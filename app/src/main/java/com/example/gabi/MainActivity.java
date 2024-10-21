package com.example.gabi;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.auxiliares.AuxiliarActivity;
import com.example.gabi.enfermeria.EnfermeriaActivity; // Importar la nueva actividad

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText usuario;
    EditText contrasena;
    Button botonEntrar;
    TextView recuperarContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.usuario);
        contrasena = findViewById(R.id.contrasena);
        botonEntrar = findViewById(R.id.botonEntrar);
        recuperarContrasena = findViewById(R.id.recuperarContrasena);

        botonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usuario.getText().toString(), contrasena.getText().toString());
            }
        });

        recuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoRecuperarContrasena();
            }
        });
    }

    private void login(final String username, final String password) {
        String url = "https://residencialontananza.com/api/login.php"; // URL del script PHP

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        if (response.startsWith("{")) {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getString("status").equals("success")) {
                                String token = jsonResponse.getString("token");
                                String nombre = jsonResponse.getString("nombre");
                                int trabajadorId = jsonResponse.getInt("user_id"); // Obtener el ID del trabajador
                                String role = jsonResponse.getString("role");

                                // Guarda el token, el nombre y el ID del trabajador en SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", token);
                                editor.putString("nombre", nombre);
                                editor.putInt("trabajador_id", trabajadorId);
                                editor.apply();

                                // Navegar a la actividad adecuada
                                Intent intent;
                                switch (role) {
                                    case "administrador":
                                        intent = new Intent(MainActivity.this, AdministradorActivity.class);
                                        break;
                                    case "auxiliar":
                                        intent = new Intent(MainActivity.this, AuxiliarActivity.class);
                                        break;
                                    case "enfermero": // Nuevo caso para enfermero
                                        intent = new Intent(MainActivity.this, EnfermeriaActivity.class);
                                        break;
                                    default:
                                        Toast.makeText(MainActivity.this, "Rol no reconocido: " + role, Toast.LENGTH_SHORT).show();
                                        return;
                                }

                                intent.putExtra("nombre", nombre); // Pasa el nombre del usuario
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Respuesta del servidor no válida: " + response, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Error en la respuesta del servidor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(MainActivity.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void mostrarDialogoRecuperarContrasena() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_recuperar_contrasena, null);
        builder.setView(dialogView);

        EditText dniEditText = dialogView.findViewById(R.id.dniEditText);
        Button btnEnviar = dialogView.findViewById(R.id.btnEnviar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        AlertDialog dialog = builder.create();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = dniEditText.getText().toString().trim();
                if (!dni.isEmpty()) {
                    enviarSolicitudRecuperacion(dni, dialog);
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, ingrese su DNI", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void enviarSolicitudRecuperacion(String dni, AlertDialog dialog) {
        String url = "https://residencialontananza.com/api/restaurarPasswordViaMail.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Toast.makeText(MainActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Error en la respuesta del servidor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(MainActivity.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("dni", dni);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
