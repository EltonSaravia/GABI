package managers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dto.TurnoDTO;

public class TurnoManager {

    private Context context;
    private String token;

    public TurnoManager(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    public void obtenerTurnosDiaActual(final TurnoCallback callback) {
        new ObtenerTurnosTask(callback).execute();
    }

    public void asignarTurno(int trabajadorId, String tipoTurno, String fecha, TurnoCallback callback) {
        new AsignarTurnoTask(trabajadorId, tipoTurno, fecha, callback).execute();
    }

    private class ObtenerTurnosTask extends AsyncTask<Void, Void, List<TurnoDTO>> {
        private TurnoCallback callback;
        private String error;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public ObtenerTurnosTask(TurnoCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<TurnoDTO> doInBackground(Void... voids) {
            List<TurnoDTO> listaTurnos = new ArrayList<>();
            try {
                URL url = new URL("https://residencialontananza.com/api/obtenerTurnosDiaActual.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + token);

                Log.d("TurnoManager", "Token: " + token); // Log del token

                int responseCode = connection.getResponseCode();
                Log.d("TurnoManager", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    reader.close();
                    Log.d("TurnoManager", "Response from API: " + result.toString());

                    JSONObject jsonResponse = new JSONObject(result.toString());
                    JSONArray jsonArray = jsonResponse.getJSONArray("turnos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TurnoDTO turno = new TurnoDTO(
                                jsonObject.getInt("id"),
                                jsonObject.getInt("trabajador_id"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("apellido_1"),
                                jsonObject.getString("apellido_2"),
                                jsonObject.getString("puesto"),
                                dateFormat.parse(jsonObject.getString("fecha_inicio")),
                                dateFormat.parse(jsonObject.getString("fecha_fin")),
                                jsonObject.getString("tipo")
                        );
                        listaTurnos.add(turno);
                    }
                } else {
                    error = "HTTP error code: " + responseCode;
                    Log.e("TurnoManager", "HTTP error code: " + responseCode);
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("TurnoManager", "Error: " + error, e);
            }
            return listaTurnos;
        }

        @Override
        protected void onPostExecute(List<TurnoDTO> turnos) {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onSuccess(turnos);
            }
        }
    }

    private class AsignarTurnoTask extends AsyncTask<Void, Void, String> {
        private int trabajadorId;
        private String tipoTurno;
        private String fecha;
        private TurnoCallback callback;
        private String error;

        public AsignarTurnoTask(int trabajadorId, String tipoTurno, String fecha, TurnoCallback callback) {
            this.trabajadorId = trabajadorId;
            this.tipoTurno = tipoTurno;
            this.fecha = fecha;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://residencialontananza.com/api/asignarTurno.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

                String postData = "trabajador_id=" + trabajadorId + "&tipo_turno=" + tipoTurno + "&fecha=" + fecha;
                OutputStream os = connection.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if (jsonResponse.getString("status").equals("success")) {
                        return jsonResponse.getString("message");
                    } else {
                        error = jsonResponse.getString("message");
                        return null;
                    }
                } else {
                    error = "HTTP error code: " + responseCode;
                    return null;
                }
            } catch (Exception e) {
                error = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onSuccess(result);
            }
        }
    }
}
