package com.example.gabi.administrador;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.R;
import dto.TrabajadorDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListarTrabajadoresFragment extends Fragment {

    private RecyclerView recyclerViewTrabajadores;
    private TrabajadoresInfoCompletaAdapter trabajadoresAdapter;
    private ArrayList<TrabajadorDTO> listaTrabajadores;

    public ListarTrabajadoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar_trabajadores, container, false);

        recyclerViewTrabajadores = view.findViewById(R.id.recyclerViewTrabajadores);
        recyclerViewTrabajadores.setLayoutManager(new LinearLayoutManager(getContext()));

        listaTrabajadores = new ArrayList<>();
        trabajadoresAdapter = new TrabajadoresInfoCompletaAdapter(listaTrabajadores);
        recyclerViewTrabajadores.setAdapter(trabajadoresAdapter);

        obtenerTrabajadores();

        return view;
    }

    private void obtenerTrabajadores() {
        String url = "https://residencialontananza.com/api/listarTrabajadores.php";

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
                            trabajadoresAdapter.notifyDataSetChanged();
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
}
