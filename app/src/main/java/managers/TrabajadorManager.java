package managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import dto.TrabajadorDTO;
import dto.TrabajadorTurnoDTO;

public class TrabajadorManager {
    private Context context;
    private String token;

    public TrabajadorManager(Context context) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        this.token = sharedPreferences.getString("token", null);
    }

    public void obtenerTrabajadoresEnJornada(TrabajadorCallback callback) {
        new ObtenerTrabajadoresTask(callback).execute();
    }

    public void buscarTrabajador(String dni, String token, TrabajadorCallback callback) {
        new BuscarTrabajadorTask(dni, token, callback).execute();
    }

    public void obtenerTrabajadoresPorFecha(String fecha, TrabajadorTurnoCallback callback) {
        new ObtenerTrabajadoresPorFechaTask(fecha, callback).execute();
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
                connection.setRequestProperty("Authorization", "Bearer " + token);

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
                    TrabajadorDTO trabajador = new TrabajadorDTO(
                            jsonObject.getInt("id"),
                            jsonObject.getString("dni"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("apellido1"),
                            jsonObject.getString("apellido2"),
                            jsonObject.getString("puesto"),
                            jsonObject.getString("telefono"),
                            jsonObject.getString("email"),
                            ""  // La contraseña no debería ser parte del DTO desde el servidor por seguridad
                    );
                    listaTrabajadores.add(trabajador);
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("TrabajadorError", "Error: " + error);
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

    private class BuscarTrabajadorTask extends AsyncTask<Void, Void, TrabajadorDTO> {
        private String dni;
        private String token;
        private TrabajadorCallback callback;
        private String error;

        public BuscarTrabajadorTask(String dni, String token, TrabajadorCallback callback) {
            this.dni = dni;
            this.token = token;
            this.callback = callback;
        }

        @Override
        protected TrabajadorDTO doInBackground(Void... voids) {
            TrabajadorDTO trabajador = null;
            try {
                URL url = new URL("https://residencialontananza.com/api/buscarTrabajadorParaActualizar.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setDoOutput(true);

                String postData = "dni=" + dni;
                OutputStream os = connection.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                Log.d("BuscarTrabajadorTask", "Response: " + result.toString());

                JSONObject jsonObject = new JSONObject(result.toString());
                if (jsonObject.getString("status").equals("success")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    trabajador = new TrabajadorDTO(
                            data.getInt("id"),
                            data.getString("dni"),
                            data.getString("nombre"),
                            data.getString("apellido_1"),
                            data.getString("apellido_2"),
                            data.getString("puesto"),
                            data.getString("telefono"),
                            data.getString("email"),
                            ""
                    );
                } else {
                    error = jsonObject.getString("message");
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("TrabajadorError", "Error: " + error);
            }
            return trabajador;
        }

        @Override
        protected void onPostExecute(TrabajadorDTO trabajador) {
            if (error != null) {
                callback.onError(error);
            } else {
                List<TrabajadorDTO> lista = new ArrayList<>();
                if (trabajador != null) {
                    lista.add(trabajador);
                }
                callback.onSuccess(lista);
            }
        }
    }

    private class ObtenerTrabajadoresPorFechaTask extends AsyncTask<Void, Void, List<TrabajadorTurnoDTO>> {
        private String fecha;
        private TrabajadorTurnoCallback callback;
        private String error;

        public ObtenerTrabajadoresPorFechaTask(String fecha, TrabajadorTurnoCallback callback) {
            this.fecha = fecha;
            this.callback = callback;
        }

        @Override
        protected List<TrabajadorTurnoDTO> doInBackground(Void... voids) {
            List<TrabajadorTurnoDTO> listaTrabajadores = new ArrayList<>();
            try {
                URL url = new URL("https://residencialontananza.com/api/obtenerTrabajadoresPorFecha.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Authorization", "Bearer " + token);  // Asegúrate de incluir el token
                connection.setDoOutput(true);

                String postData = "fecha=" + URLEncoder.encode(fecha, "UTF-8");
                Log.d("TrabajadorManager", "postData: " + postData);
                OutputStream os = connection.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();

                int responseCode = connection.getResponseCode();
                Log.d("TrabajadorManager", "Response Code: " + responseCode);
                Log.d("TrabajadorManager", "Response: " + result.toString());

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    JSONObject jsonResponse = new JSONObject(result.toString());
                    if (jsonResponse.getString("status").equals("success")) {
                        JSONArray jsonArray = jsonResponse.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            TrabajadorTurnoDTO trabajador = new TrabajadorTurnoDTO(
                                    jsonObject.getInt("id"), // Extraer el id
                                    jsonObject.getString("nombre"),
                                    jsonObject.getString("apellido_1"),
                                    jsonObject.getString("apellido_2"),
                                    jsonObject.getString("puesto"),
                                    jsonObject.getString("turno")
                            );
                            listaTrabajadores.add(trabajador);
                        }
                    } else {
                        error = jsonResponse.getString("message");
                    }
                } else {
                    error = "HTTP error code: " + responseCode;
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("TrabajadorError", "Error: " + error, e);
            }
            return listaTrabajadores;
        }

        @Override
        protected void onPostExecute(List<TrabajadorTurnoDTO> trabajadores) {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onSuccess(trabajadores);
            }
        }
    }

    public interface TrabajadorTurnoCallback {
        void onSuccess(List<TrabajadorTurnoDTO> trabajadores);

        void onError(String error);
    }

    public void obtenerAuxiliares(TrabajadorCallback callback) {
        new ObtenerAuxiliaresTask(callback).execute();
    }

    private class ObtenerAuxiliaresTask extends AsyncTask<Void, Void, List<TrabajadorDTO>> {
        private TrabajadorCallback callback;
        private String error;

        public ObtenerAuxiliaresTask(TrabajadorCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<TrabajadorDTO> doInBackground(Void... voids) {
            List<TrabajadorDTO> listaAuxiliares = new ArrayList<>();
            try {
                URL url = new URL("https://residencialontananza.com/api/obtenerAuxiliares.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + token);

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
                    TrabajadorDTO auxiliar = new TrabajadorDTO(
                            jsonObject.getInt("id"),
                            jsonObject.getString("dni"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("apellido_1"),
                            jsonObject.getString("apellido_2"),
                            jsonObject.getString("puesto"),
                            jsonObject.getString("telefono"),
                            jsonObject.getString("email"),
                            ""  // La contraseña no debería ser parte del DTO desde el servidor por seguridad
                    );
                    listaAuxiliares.add(auxiliar);
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("TrabajadorError", "Error: " + error);
            }
            return listaAuxiliares;
        }

        @Override
        protected void onPostExecute(List<TrabajadorDTO> auxiliares) {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onSuccess(auxiliares);
            }
        }
    }
}
