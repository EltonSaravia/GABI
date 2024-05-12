package com.example.gabi.managers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dto.TrabajadorDTO;

public class TrabajadorManager {
    private Context context;

    public TrabajadorManager(Context context) {
        this.context = context;
    }

    public void obtenerTrabajadoresEnJornada() {
        String url = "https://residencialontananza.com/api/obtenerTrabajadoresEnJornada.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        ArrayList<TrabajadorDTO> trabajadores = new ArrayList<>();
                        JSONArray array = response.getJSONArray("trabajadores");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String nombre = obj.getString("nombre");
                            String apellido1 = obj.getString("apellido1");
                            String puesto = obj.getString("puesto");
                            trabajadores.add(new TrabajadorDTO(0, "", nombre, apellido1, "", puesto, "", "", ""));
                        }
                        // Aquí deberías actualizar la interfaz con esta lista
                        // Por ejemplo, si estás en una Activity o Fragment puedes llamar a un método que actualice el adaptador
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }
}
