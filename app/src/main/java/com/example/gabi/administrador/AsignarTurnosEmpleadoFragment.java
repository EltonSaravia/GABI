package com.example.gabi.administrador;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.R;
import com.example.gabi.AdaptadorTrabajador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dto.TrabajadorDTO;
import dto.TurnoDTO;
import managers.TurnoManager;
import managers.TurnoCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AsignarTurnosEmpleadoFragment extends Fragment {

    private TextView textViewTitulo, textViewNombreEmpleado;
    private RadioGroup radioGroupTurno;
    private RadioButton radioButtonDiurno, radioButtonVespertino, radioButtonNocturno;
    private Button btnAsignarTurno;
    private RecyclerView recyclerViewTrabajadores;
    private TrabajadorDTO trabajadorSeleccionado;
    private String fechaSeleccionada;

    private List<TrabajadorDTO> listaTrabajadores;
    private AdaptadorTrabajador adaptadorTrabajador;

    public AsignarTurnosEmpleadoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignar_turnos_empleado, container, false);

        textViewTitulo = view.findViewById(R.id.tituloTexto);
        textViewNombreEmpleado = view.findViewById(R.id.textViewNombreEmpleado);  // Asegúrate de que este ID existe en tu layout
        radioGroupTurno = view.findViewById(R.id.radioGroupTurno);
        radioButtonDiurno = view.findViewById(R.id.radioButtonDiurno);
        radioButtonVespertino = view.findViewById(R.id.radioButtonVespertino);
        radioButtonNocturno = view.findViewById(R.id.radioButtonNocturno);
        btnAsignarTurno = view.findViewById(R.id.btnAsignarTurno);
        recyclerViewTrabajadores = view.findViewById(R.id.recyclerViewTrabajadores);

        listaTrabajadores = new ArrayList<>();
        adaptadorTrabajador = new AdaptadorTrabajador(getContext(), listaTrabajadores);

        recyclerViewTrabajadores.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTrabajadores.setAdapter(adaptadorTrabajador);

        if (getArguments() != null) {
            fechaSeleccionada = getArguments().getString("fechaSeleccionada");
        }

        adaptadorTrabajador.setOnItemClickListener(new AdaptadorTrabajador.OnItemClickListener() {
            @Override
            public void onItemClick(TrabajadorDTO trabajador) {
                trabajadorSeleccionado = trabajador;
                textViewNombreEmpleado.setText(trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());
            }
        });

        btnAsignarTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignarTurno();
            }
        });

        obtenerTrabajadores();

        return view;
    }

    private void obtenerTrabajadores() {
        String url = "https://residencialontananza.com/api/listarTrabajadores.php";

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", "");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            listaTrabajadores.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                TrabajadorDTO trabajador = new TrabajadorDTO(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("dni"),
                                        jsonObject.getString("nombre"),
                                        jsonObject.getString("apellido_1"),
                                        jsonObject.getString("apellido_2"),
                                        jsonObject.getString("puesto"),
                                        jsonObject.getString("telefono"),
                                        jsonObject.getString("email"),
                                        "" // Dejar el campo contrasena vacío o ajustar según necesidad
                                );
                                listaTrabajadores.add(trabajador);
                            }
                            adaptadorTrabajador.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Error: " + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error en la respuesta del servidor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private void asignarTurno() {
        if (trabajadorSeleccionado == null) {
            Toast.makeText(getContext(), "Por favor seleccione un trabajador", Toast.LENGTH_SHORT).show();
            return;
        }

        String turno = "";
        int selectedId = radioGroupTurno.getCheckedRadioButtonId();
        if (selectedId == radioButtonDiurno.getId()) {
            turno = "diurno";
        } else if (selectedId == radioButtonVespertino.getId()) {
            turno = "vespertino";
        } else if (selectedId == radioButtonNocturno.getId()) {
            turno = "nocturno";
        }

        if (turno.isEmpty()) {
            Toast.makeText(getContext(), "Por favor seleccione un turno", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        TurnoManager turnoManager = new TurnoManager(getContext(), token);
        turnoManager.asignarTurno(trabajadorSeleccionado.getId(), turno, fechaSeleccionada, new TurnoCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Turno asignado: " + message, Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error al asignar turno: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<TurnoDTO> turnos) {
                // No se usa aquí
            }
        });
    }
}
