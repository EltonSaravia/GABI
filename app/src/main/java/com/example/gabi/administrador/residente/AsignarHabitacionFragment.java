package com.example.gabi.administrador.residente;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.R;
import dto.HabitacionDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsignarHabitacionFragment extends Fragment {

    private RecyclerView recyclerViewHabitaciones;
    private Button btnConfirmarAsignacion;
    private int selectedHabitacionId = -1;
    private int residenteId;
    private String token;

    public AsignarHabitacionFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignar_habitacion, container, false);

        recyclerViewHabitaciones = view.findViewById(R.id.recyclerViewHabitaciones);
        btnConfirmarAsignacion = view.findViewById(R.id.btnConfirmarAsignacion);

        // Obtener el token desde SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        if (getArguments() != null) {
            residenteId = getArguments().getInt("residente_id");
        }

        recyclerViewHabitaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        cargarHabitaciones();

        btnConfirmarAsignacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedHabitacionId != -1) {
                    asignarHabitacion();
                } else {
                    Toast.makeText(getContext(), "Seleccione una habitación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void cargarHabitaciones() {
        String url = "https://residencialontananza.com/api/listarHabitaciones.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<HabitacionDTO> habitaciones = new ArrayList<>();
                        try {
                            // Verifica si la respuesta contiene el campo "status" y si es "success"
                            if (response.getString("status").equals("success")) {
                                // Extrae el JSONArray de la respuesta
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject habitacion = jsonArray.getJSONObject(i);
                                    int id = habitacion.getInt("id");
                                    int numero = habitacion.getInt("numero");
                                    String piso = habitacion.getString("piso");
                                    String observaciones = habitacion.getString("observaciones");
                                    boolean ocupada = habitacion.getInt("ocupada") == 1;

                                    habitaciones.add(new HabitacionDTO(id, numero, piso, observaciones, ocupada));
                                }

                                HabitacionAdapter adapter = new HabitacionAdapter(habitaciones, new HabitacionAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(HabitacionDTO habitacion) {
                                        selectedHabitacionId = habitacion.getId();
                                        Toast.makeText(getContext(), "Habitación seleccionada: " + habitacion.getNumero(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                recyclerViewHabitaciones.setAdapter(adapter);
                            } else {
                                // Maneja el caso en que el status no sea "success"
                                Toast.makeText(getContext(), "Error en la respuesta: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al cargar habitaciones: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    private void asignarHabitacion() {
        String url = "https://residencialontananza.com/api/asignarHabitacion.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Habitación asignada correctamente", Toast.LENGTH_SHORT).show();
                        // Código para manejar la respuesta y navegar a la siguiente pantalla
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al asignar habitación: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("residente_id", String.valueOf(residenteId));
                params.put("habitacion_id", String.valueOf(selectedHabitacionId));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
