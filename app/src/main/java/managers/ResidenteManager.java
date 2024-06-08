package managers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import dto.ResidenteDTO;

public class ResidenteManager {

    private Context context;

    public ResidenteManager(Context context) {
        this.context = context;
    }

    public void buscarResidente(String dni, String token, ResidenteCallback callback) {
        new BuscarResidenteTask(dni, token, callback).execute();
    }

    private class BuscarResidenteTask extends AsyncTask<Void, Void, ResidenteDTO> {
        private String dni;
        private String token;
        private ResidenteCallback callback;
        private String error;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        public BuscarResidenteTask(String dni, String token, ResidenteCallback callback) {
            this.dni = dni;
            this.token = token;
            this.callback = callback;
        }

        @Override
        protected ResidenteDTO doInBackground(Void... voids) {
            ResidenteDTO residente = null;
            try {
                URL url = new URL("https://residencialontananza.com/api/buscarResidente.php");
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
                JSONObject jsonObject = new JSONObject(result.toString());
                if (jsonObject.getString("status").equals("success")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    residente = new ResidenteDTO(
                            data.getInt("id"),
                            data.getString("dni"),
                            data.getString("nombre"),
                            data.getString("apellidos"),
                            dateFormat.parse(data.getString("fecha_nacimiento")),
                            data.getString("ar"),
                            data.getString("nss"),
                            data.getString("numero_cuenta_bancaria"),
                            data.getString("observaciones"),
                            data.getInt("medicamentos"),
                            dateFormat.parse(data.getString("fecha_ingreso")),
                            data.getString("activo"),
                            data.getString("empadronamiento"),
                            data.has("foto") && !data.isNull("foto") ? Base64.decode(data.getString("foto"), Base64.DEFAULT) : null,
                            data.has("habitacion_id") && !data.isNull("habitacion_id") ? data.getInt("habitacion_id") : null,  // Manejo de null
                            data.getBoolean("estado"),
                            data.getString("email"),
                            data.getString("telefono"),
                            data.has("tlfn_familiar_1") && !data.isNull("tlfn_familiar_1") ? data.getString("tlfn_familiar_1") : "+34",  // Valor por defecto
                            data.has("tlfn_familiar_2") && !data.isNull("tlfn_familiar_2") ? data.getString("tlfn_familiar_2") : "+34"   // Valor por defecto
                    );
                } else {
                    error = jsonObject.getString("message");
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("ResidenteError", "Error: " + error);
            }
            return residente;
        }

        @Override
        protected void onPostExecute(ResidenteDTO residente) {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onSuccess(residente);
            }
        }
    }
}
