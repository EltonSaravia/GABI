package com.example.gabi.administrador.residente;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.R;
import dto.ResidenteDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarResidentesFragment extends Fragment {

    private RecyclerView recyclerViewResidentes;
    private ResidenteAdapter residenteAdapter;
    private ArrayList<ResidenteDTO> listaResidentes;
    private ArrayList<ResidenteDTO> listaResidentesFiltrada;
    private EditText searchEditText;

    public ListarResidentesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar_residentes, container, false);

        recyclerViewResidentes = view.findViewById(R.id.recyclerViewResidentes);
        recyclerViewResidentes.setLayoutManager(new LinearLayoutManager(getContext()));

        searchEditText = view.findViewById(R.id.searchEditText);
        listaResidentes = new ArrayList<>();
        listaResidentesFiltrada = new ArrayList<>();
        residenteAdapter = new ResidenteAdapter(listaResidentesFiltrada);
        recyclerViewResidentes.setAdapter(residenteAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarResidentes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        obtenerResidentes();

        return view;
    }

    private void obtenerResidentes() {
        String url = "https://residencialontananza.com/api/listarResidentes.php";

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", "");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                int id = jsonObject.getInt("id");
                                String dni = jsonObject.getString("dni");
                                String nombre = jsonObject.getString("nombre");
                                String apellidos = jsonObject.getString("apellidos");
                                String fechaNacimientoStr = jsonObject.getString("fecha_nacimiento");
                                java.sql.Date fechaNacimiento = fechaNacimientoStr.equals("0000-00-00") ? null : java.sql.Date.valueOf(fechaNacimientoStr);
                                String ar = jsonObject.getString("ar");
                                String nss = jsonObject.getString("nss");
                                String numeroCuentaBancaria = jsonObject.getString("numero_cuenta_bancaria");
                                String observaciones = jsonObject.getString("observaciones");
                                int medicamentos = jsonObject.isNull("medicamentos") ? 0 : jsonObject.getInt("medicamentos");
                                String fechaIngresoStr = jsonObject.getString("fecha_ingreso");
                                java.sql.Date fechaIngreso = fechaIngresoStr.equals("0000-00-00") ? null : java.sql.Date.valueOf(fechaIngresoStr);
                                String activo = jsonObject.getString("activo");
                                String empadronamiento = jsonObject.getString("empadronamiento");
                                int edad = jsonObject.isNull("edad") ? 0 : jsonObject.getInt("edad");
                                int mesCumple = jsonObject.isNull("mes_cumple") ? 0 : jsonObject.getInt("mes_cumple");
                                byte[] foto = null; // Manejar la conversión de blob según sea necesario
                                int habitacionId = jsonObject.isNull("habitacion_id") ? 0 : jsonObject.getInt("habitacion_id");
                                boolean estado = jsonObject.getInt("estado") == 1;
                                String telefono = jsonObject.getString("telefono");
                                String email = jsonObject.getString("email");

                                ResidenteDTO residente = new ResidenteDTO(id, dni, nombre, apellidos, fechaNacimiento, ar, nss, numeroCuentaBancaria, observaciones, medicamentos, fechaIngreso, activo, empadronamiento, edad, mesCumple, foto, habitacionId, estado, telefono, email);
                                listaResidentes.add(residente);
                            }
                            listaResidentesFiltrada.addAll(listaResidentes);
                            residenteAdapter.notifyDataSetChanged();
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

    private void filtrarResidentes(String texto) {
        listaResidentesFiltrada.clear();
        if (texto.isEmpty()) {
            listaResidentesFiltrada.addAll(listaResidentes);
        } else {
            for (ResidenteDTO residente : listaResidentes) {
                if (residente.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                        residente.getApellidos().toLowerCase().contains(texto.toLowerCase()) ||
                        residente.getEmail().toLowerCase().contains(texto.toLowerCase()) ||
                        residente.getDni().toLowerCase().contains(texto.toLowerCase())) {
                    listaResidentesFiltrada.add(residente);
                }
            }
        }
        residenteAdapter.notifyDataSetChanged();
    }
}
