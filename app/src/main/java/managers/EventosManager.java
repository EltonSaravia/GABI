package managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.EventoDTO;

public class EventosManager {
    private Context context;

    public EventosManager(Context context) {
        this.context = context;
    }

    public void obtenerEventosDelDia(EventosCallback callback) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String url = "https://residencialontananza.com/api/obtenerEventosDelDia.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        List<EventoDTO> listaEventos = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Date fechaCita = dateFormat.parse(jsonObject.getString("fecha_cita"));
                            Date horaCita = timeFormat.parse(jsonObject.getString("hora_cita"));

                            EventoDTO evento = new EventoDTO(
                                    jsonObject.getInt("id"),
                                    jsonObject.getInt("residente_id"),
                                    fechaCita,
                                    new java.sql.Time(horaCita.getTime()),
                                    jsonObject.getString("lugar_cita"),
                                    jsonObject.getString("motivo_cita"),
                                    jsonObject.getString("detalles"),
                                    jsonObject.getString("nombre_residente"),
                                    jsonObject.getString("apellidos_residente")
                            );
                            listaEventos.add(evento);
                        }

                        callback.onSuccess(listaEventos);

                    } catch (Exception e) {
                        Log.e("EventosError", "Error parsing JSON: " + e.getMessage());
                        callback.onError(e.getMessage());
                    }
                },
                error -> {
                    Log.e("EventosError", "Error de red: " + error.getMessage());
                    callback.onError(error.getMessage());
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
}
