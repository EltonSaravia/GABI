package managers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
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
                URL url = new URL("https://residencialontananza.com/api/obtenerEventosDelDia.php");
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
                // Definir el formato de fecha y hora que coincide con el formato en tu JSON
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    try {
                        // Parsear fecha y hora correctamente
                        Date fechaCita = dateFormat.parse(jsonObject.getString("fecha_cita"));
                        Date horaCita = timeFormat.parse(jsonObject.getString("hora_cita"));

                        EventoDTO evento = new EventoDTO(
                                jsonObject.getInt("id"),
                                jsonObject.getInt("residente_id"),
                                fechaCita,
                                new java.sql.Time(horaCita.getTime()),
                                jsonObject.getString("lugar_cita"),
                                jsonObject.getString("motivo_cita"),
                                jsonObject.getString("detalles")
                        );
                        listaEventos.add(evento);
                    } catch (ParseException e) {
                        // Manejar error de parseo
                        error = e.getMessage();
                        Log.e("EventosError", "Error parsing date or time: " + error);
                    }
                }
            } catch (Exception e) {
                error = e.getMessage();
            }
            return listaEventos;
        }

        @Override
        protected void onPostExecute(List<EventoDTO> eventos) {
            if (error != null) {
                Log.e("EventosError", "Error: " + error);
                callback.onError(error);
            } else {
                Log.d("Eventos", "Eventos recibidos: " + eventos.size());
                callback.onSuccess(eventos);
            }
        }
    }
}
