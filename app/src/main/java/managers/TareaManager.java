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
import java.util.ArrayList;
import java.util.List;

import dto.TareaDTO;

public class TareaManager {
    private Context context;
    private String token;

    public TareaManager(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    public void asignarTarea(int trabajadorId, String titulo, String notas, String fechaTareaAsignada, String horaTareaAsignada, TareaCallback callback) {
        new AsignarTareaTask(trabajadorId, titulo, notas, fechaTareaAsignada, horaTareaAsignada, callback).execute();
    }

    public void obtenerTareasAsignadas(int trabajadorId, TareaCallback callback) {
        new ObtenerTareasAsignadasTask(trabajadorId, callback).execute();
    }

    public void obtenerTareasParaAuxiliar(int trabajadorId, TareaCallback callback) { // Método nuevo para la API especificada
        new ObtenerTareasParaAuxiliarTask(trabajadorId, callback).execute();
    }

    public void completarTarea(int tareaId, String notas, TareaCallback callback) {
        new CompletarTareaTask(tareaId, notas, callback).execute();
    }

    private class AsignarTareaTask extends AsyncTask<Void, Void, String> {
        private int trabajadorId;
        private String titulo;
        private String notas;
        private String fechaTareaAsignada;
        private String horaTareaAsignada;
        private TareaCallback callback;
        private String error;

        public AsignarTareaTask(int trabajadorId, String titulo, String notas, String fechaTareaAsignada, String horaTareaAsignada, TareaCallback callback) {
            this.trabajadorId = trabajadorId;
            this.titulo = titulo;
            this.notas = notas;
            this.fechaTareaAsignada = fechaTareaAsignada;
            this.horaTareaAsignada = horaTareaAsignada;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://residencialontananza.com/api/asignarTareaAAuxiliar.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setDoOutput(true);

                String postData = "trabajador_id=" + trabajadorId + "&titulo=" + titulo + "&notas=" + notas + "&fecha_tarea_asignada=" + fechaTareaAsignada + "&hora_tarea_asignada=" + horaTareaAsignada;
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
                JSONObject jsonResponse = new JSONObject(result.toString());

                if (jsonResponse.getString("status").equals("success")) {
                    return jsonResponse.getString("message");
                } else {
                    error = jsonResponse.getString("message");
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("TareaError", "Error: " + error, e);
            }
            return null;
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

    private class ObtenerTareasAsignadasTask extends AsyncTask<Void, Void, List<TareaDTO>> {
        private int trabajadorId;
        private TareaCallback callback;
        private String error;

        public ObtenerTareasAsignadasTask(int trabajadorId, TareaCallback callback) {
            this.trabajadorId = trabajadorId;
            this.callback = callback;
        }

        @Override
        protected List<TareaDTO> doInBackground(Void... voids) {
            List<TareaDTO> tareaList = new ArrayList<>();
            try {
                URL url = new URL("https://residencialontananza.com/api/obtenerTareasPorTrabajador.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setDoOutput(true);

                String postData = "trabajador_id=" + trabajadorId;
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
                JSONObject jsonResponse = new JSONObject(result.toString());

                if (jsonResponse.getString("status").equals("success")) {
                    JSONArray jsonArray = jsonResponse.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TareaDTO tarea = new TareaDTO(
                                jsonObject.getInt("id"),
                                jsonObject.getString("titulo"),
                                jsonObject.getString("notas"),
                                jsonObject.getString("fecha_tarea_asignada"),
                                jsonObject.getString("hora_tarea_asignada"),
                                jsonObject.getInt("trabajador_id"),
                                jsonObject.getInt("estado") // Añadido
                        );
                        tareaList.add(tarea);
                    }
                } else {
                    error = jsonResponse.getString("message");
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("TareaError", "Error: " + error, e);
            }
            return tareaList;
        }

        @Override
        protected void onPostExecute(List<TareaDTO> result) {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onSuccess(result);
            }
        }
    }

    private class ObtenerTareasParaAuxiliarTask extends AsyncTask<Void, Void, List<TareaDTO>> { // Clase nueva para la API especificada
        private int trabajadorId;
        private TareaCallback callback;
        private String error;

        public ObtenerTareasParaAuxiliarTask(int trabajadorId, TareaCallback callback) {
            this.trabajadorId = trabajadorId;
            this.callback = callback;
        }

        @Override
        protected List<TareaDTO> doInBackground(Void... voids) {
            List<TareaDTO> tareaList = new ArrayList<>();
            try {
                URL url = new URL("https://residencialontananza.com/aux/obtenerTareasParaAuxiliar.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setDoOutput(true);

                String postData = "trabajador_id=" + trabajadorId;
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
                JSONObject jsonResponse = new JSONObject(result.toString());

                if (jsonResponse.getString("status").equals("success")) {
                    JSONArray jsonArray = jsonResponse.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TareaDTO tarea = new TareaDTO(
                                jsonObject.getInt("id"),
                                jsonObject.getString("titulo"),
                                jsonObject.getString("notas"),
                                jsonObject.getInt("estado"), // Añadido
                                trabajadorId
                        );
                        tareaList.add(tarea);
                    }
                } else {
                    error = jsonResponse.getString("message");
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("TareaError", "Error: " + error, e);
            }
            return tareaList;
        }

        @Override
        protected void onPostExecute(List<TareaDTO> result) {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onSuccess(result);
            }
        }
    }

    private class CompletarTareaTask extends AsyncTask<Void, Void, String> {
        private int tareaId;
        private String notas;
        private TareaCallback callback;
        private String error;

        public CompletarTareaTask(int tareaId, String notas, TareaCallback callback) {
            this.tareaId = tareaId;
            this.notas = notas;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://residencialontananza.com/api/tareaMarcarCompletada.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.setDoOutput(true);

                String postData = "tarea_id=" + tareaId + "&notas=" + notas + "&estado=1";
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
                JSONObject jsonResponse = new JSONObject(result.toString());

                if (jsonResponse.getString("status").equals("success")) {
                    return jsonResponse.getString("message");
                } else {
                    error = jsonResponse.getString("message");
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("TareaError", "Error: " + error, e);
            }
            return null;
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
