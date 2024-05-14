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
import java.util.List;

import dto.EventoDTO;

public class EventosManager {
    private Context context;

    public EventosManager(Context context) {
        this.context = context;
    }

    public void obtenerEventosDelDia(EventosCallback callback) {
        new ObtenerEventosTask(callback).execute();
    }

    private class ObtenerEventosTask extends AsyncTask<Void, Void, List<EventoDTO>> {
        private EventosCallback callback;
        private String error;

        public ObtenerEventosTask(EventosCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<EventoDTO> doInBackground(Void... voids) {
            List<EventoDTO> listaEventos = new ArrayList<>();
            try {
                URL url = new URL("https://residencialontananza.com/api/obtenerEventosDiaActual.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();

                JSONArray jsonArray = new JSONArray(result.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    EventoDTO evento = new EventoDTO(
                            jsonObject.getInt("id"),
                            jsonObject.getInt("residente_id"),
                            new java.util.Date(jsonObject.getLong("fecha_cita")),
                            new java.sql.Time(jsonObject.getLong("hora_cita")),
                            jsonObject.getString("lugar_cita"),
                            jsonObject.getString("motivo_cita"),
                            jsonObject.getString("detalles")
                    );
                    listaEventos.add(evento);
                }
            } catch (Exception e) {
                error = e.getMessage();
            }
            return listaEventos;
        }

        @Override
        protected void onPostExecute(List<EventoDTO> eventos) {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onSuccess(eventos);
            }
        }
    }
}
