package com.example.gabi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText usuario;
    EditText contrasena;
    Button botonEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.usuario);
        contrasena = findViewById(R.id.contrasena);
        botonEntrar = findViewById(R.id.botonEntrar);

        botonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usuario.getText().toString(), contrasena.getText().toString());
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
                            Log.d("LoginResponse", response); // Log the response for debugging
                            if (jsonResponse.getString("status").equals("success")) {
                                String token = jsonResponse.getString("token");
                                String nombre = jsonResponse.getString("nombre");

                                // Guarda el token y el nombre en SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", token);
                                editor.putString("nombre", nombre);
                                editor.apply();

                                // Navegar a la actividad adecuada
                                if (jsonResponse.getString("role").equals("administrador")) {
                                    Intent intent = new Intent(MainActivity.this, AdministradorActivity.class);
                                    intent.putExtra("nombre", nombre); // Pasa el nombre del usuario
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Respuesta del servidor no válida: " + response, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Error en la respuesta del servidor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LoginError", "Error parsing JSON: " + e.getMessage()); // Log the error for debugging
                    }
                },
                error -> {
                    Toast.makeText(MainActivity.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("NetworkError", "Error in network request: " + error.getMessage()); // Log the error for debugging
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
}
