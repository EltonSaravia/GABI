package com.example.gabi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                // Simulación de obtener el usuario y su rol
                String user = usuario.getText().toString();
                String pwd = contrasena.getText().toString();

                // Simulación de la lógica de autenticación
                if (authenticate(user, pwd)) {
                    String userRole = getUserRole(user);  // Supón que esto devuelve el rol del usuario

                    // Redirigir según el rol
                    if (userRole.equals("administrador")) {
                        Intent intent = new Intent(MainActivity.this, AdministradorActivity.class);
                        startActivity(intent);
                        finish();  // Cierra MainActivity
                    } else {
                        // Posibles otras redirecciones
                        Toast.makeText(MainActivity.this, "Acceso no permitido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Fallo acceso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Simulación de método de autenticación
    private boolean authenticate(String user, String pwd) {
        return user.equals("admin") && pwd.equals("1234");  // Simulación de credenciales correctas
    }

    // Simulación de obtener el rol del usuario
    private String getUserRole(String user) {
        return "administrador";  // Simula que todos los usuarios son administradores
    }
}
