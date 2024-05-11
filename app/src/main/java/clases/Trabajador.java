package clases;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import dto.TrabajadorDTO;

public class Trabajador {
    private Context context;

    public Trabajador(Context context) {
        this.context = context;
    }

    public void addTrabajador(TrabajadorDTO trabajadorDTO) {
        String url = "https://residencialontananza.com/api/insertarTrabajador.php";
        JSONObject params = new JSONObject();
        try {
            params.put("dni", trabajadorDTO.getDni());
            params.put("nombre", trabajadorDTO.getNombre());
            params.put("apellido1", trabajadorDTO.getApellido1());
            params.put("apellido2", trabajadorDTO.getApellido2());
            params.put("puesto", trabajadorDTO.getPuesto());
            params.put("telefono", trabajadorDTO.getTelefono());
            params.put("email", trabajadorDTO.getEmail());
            params.put("contrasena", trabajadorDTO.getContrasena());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> Log.d("Volley", "Response: " + response.toString()),
                error -> Log.d("Volley", "Error: " + error.toString())
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }
}
