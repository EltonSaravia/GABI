package managers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import dto.EventoDTO;

public class EventosManager {
    private Context context;

    public EventosManager(Context context) {
        this.context = context;
    }

    public void obtenerEventosDelDia() {
        String url = "https://residencialontananza.com/api/obtenerEventosDelDia.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<EventoDTO> eventos = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                eventos.add(new EventoDTO(
                                        obj.getInt("id"),
                                        obj.getInt("residente_id"),
                                        java.sql.Date.valueOf(obj.getString("fecha_cita")),
                                        java.sql.Time.valueOf(obj.getString("hora_cita")),
                                        obj.getString("lugar_cita"),
                                        obj.getString("motivo_cita"),
                                        obj.getString("detalles")
                                ));
                            }
                            // Aquí actualizas la UI con los datos, como actualizar un RecyclerView
                        } catch (Exception e) {
                            Toast.makeText(context, "Error al parsear los datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);
    }
}
