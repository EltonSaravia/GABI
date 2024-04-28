package com.example.gabi;

import androidx.appcompat.app.AppCompatActivity;

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
        //Inicializacion de los componentes usados en la interfaz
        usuario = findViewById(R.id.usuario);
        contrasena = findViewById(R.id.contrasena);
        botonEntrar = findViewById(R.id.botonEntrar);
        //Logica tras precionar el boton de entrar
        botonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logica de login, tomar datos de ususario insertado y contrasena
                if (usuario.getText().toString().equals("ususario")){
                    Toast.makeText(MainActivity.this, "Correcto", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "falo accesio", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}