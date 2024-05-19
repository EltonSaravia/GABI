package managers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TareaManager {

    private Context context;
    private String token;

    public TareaManager(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    public void asignarTarea(int trabajadorId, String titulo, String notas, String fecha, String hora, final TareaCallback callback) {
        new AsignarTareaTask(trabajadorId, titulo, notas, fecha, hora, callback).execute();
    }

    private class AsignarTareaTask extends AsyncTask<Void, Void, String> {
        private int trabajadorId;
        private String titulo;
        private String notas;
        private String fecha;
        private String hora;
        private TareaCallback callback;

        public AsignarTareaTask(int trabajadorId, String titulo, String notas, String fecha, String hora, TareaCallback callback) {
            this.trabajadorId = trabajadorId;
            this.titulo = titulo;
            this.notas = notas;
            this.fecha = fecha;
            this.hora = hora;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://residencialontananza.com/api/asignarTareaAAuxiliar.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                Map<String, String> params = new HashMap<>();
                params.put("trabajador_id", String.valueOf(trabajadorId));
                params.put("titulo", titulo);
                params.put("notas", notas);
                params.put("fecha_tarea_asignada", fecha);
                params.put("hora_tarea_asignada", hora);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(param.getKey()).append('=').append(param.getValue());
                }

                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                connection.setDoOutput(true);
                try (BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream())) {
                    out.write(postDataBytes);
                    out.flush();
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return response.toString();
                } else {
                    return "Error: " + responseCode;
                }

            } catch (Exception e) {
                Log.e("TareaManager", "Error: " + e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    if (jsonResponse.getString("status").equals("success")) {
                        callback.onSuccess(jsonResponse.getString("message"));
                    } else {
                        callback.onError(jsonResponse.getString("message"));
                    }
                } catch (Exception e) {
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            } else {
                callback.onError("Error in API call");
            }
        }
    }

    public interface TareaCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}
