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

import dto.TrabajadorDTO;

public class TrabajadorManager {
    private Context context;

    public TrabajadorManager(Context context) {
        this.context = context;
    }

    public interface TrabajadorCallback {
        void onSuccess(List<TrabajadorDTO> listaTrabajadores);
        void onError(String error);
    }

    public void obtenerTrabajadoresEnJornada(TrabajadorCallback callback) {
        new ObtenerTrabajadoresTask(callback).execute();
    }

    private class ObtenerTrabajadoresTask extends AsyncTask<Void, Void, List<TrabajadorDTO>> {
        private TrabajadorCallback callback;
        private String error;

        public ObtenerTrabajadoresTask(TrabajadorCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<TrabajadorDTO> doInBackground(Void... voids) {
            List<TrabajadorDTO> listaTrabajadores = new ArrayList<>();
            try {
                URL url = new URL("https://residencialontananza.com/api/obtenerTrabajadoresEnJornada.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();

                // Parsear el resultado JSON y añadir los trabajadores a la lista
                JSONArray jsonArray = new JSONArray(result.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    TrabajadorDTO trabajador = new TrabajadorDTO(
                            0,  // ID no está disponible en la respuesta
                            "", // DNI no está disponible en la respuesta
                            jsonObject.getString("nombre"),
                            jsonObject.getString("apellido1"),
                            "", // apellido_2 no está disponible en la respuesta
                            jsonObject.getString("puesto"),
                            "", // teléfono no está disponible en la respuesta
                            "", // email no está disponible en la respuesta
                            ""  // contraseña no está disponible en la respuesta
                    );
                    listaTrabajadores.add(trabajador);
                }
            } catch (Exception e) {
                error = e.getMessage();
            }
            return listaTrabajadores;
        }

        @Override
        protected void onPostExecute(List<TrabajadorDTO> trabajadores) {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onSuccess(trabajadores);
            }
        }
    }
}
