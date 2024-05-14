package managers;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dto.TurnoDTO;

public class TurnoManager {
    private Context context;

    public TurnoManager(Context context) {
        this.context = context;
    }

    public interface TurnoCallback {
        void onSuccess(List<TurnoDTO> listaTurnos);
        void onError(String error);
    }

    public void obtenerTurnosDelDia(TurnoCallback callback) {
        new ObtenerTurnosTask(callback).execute();
    }

    private class ObtenerTurnosTask extends AsyncTask<Void, Void, List<TurnoDTO>> {
        private TurnoCallback callback;
        private String error;

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

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();

                // Parsear el resultado JSON y añadir los turnos a la lista
                JSONObject jsonResponse = new JSONObject(result.toString());
                JSONArray jsonArray = jsonResponse.getJSONArray("turnos");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    TurnoDTO turno = new TurnoDTO(
                            jsonObject.getInt("id"),
                            jsonObject.getInt("trabajador_id"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("apellido1"),
                            "",  // apellido_2 no está disponible en la respuesta
                            jsonObject.getString("puesto"),
                            new Date(jsonObject.getString("fecha_inicio")),
                            new Date(jsonObject.getString("fecha_fin")),
                            jsonObject.getString("tipo")
                    );
                    listaTurnos.add(turno);
                }
            } catch (Exception e) {
                error = e.getMessage();
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
}
