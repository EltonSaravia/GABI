package com.example.gabi.enfermeria;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gabi.R;

public class FormularioTratamientoActivity extends AppCompatActivity {

    private EditText editTextDosis, editTextUnidad, editTextFrecuencia, editTextFechaInicio, editTextFechaFin, editTextInstrucciones;
    private Button btnGuardar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_tratamiento);

        // Inicializar los campos del formulario
        editTextDosis = findViewById(R.id.editTextDosis);
        editTextUnidad = findViewById(R.id.editTextUnidad);
        editTextFrecuencia = findViewById(R.id.editTextFrecuencia);
        editTextFechaInicio = findViewById(R.id.editTextFechaInicio);
        editTextFechaFin = findViewById(R.id.editTextFechaFin);
        editTextInstrucciones = findViewById(R.id.editTextInstrucciones);

        btnGuardar = findViewById(R.id.btnGuardar);

        // Manejar el clic en el botón "Guardar"
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes implementar la lógica para guardar los datos en la base de datos
                String dosis = editTextDosis.getText().toString();
                String unidad = editTextUnidad.getText().toString();
                String frecuencia = editTextFrecuencia.getText().toString();
                String fechaInicio = editTextFechaInicio.getText().toString();
                String fechaFin = editTextFechaFin.getText().toString();
                String instrucciones = editTextInstrucciones.getText().toString();

                // Validar los campos y realizar las acciones necesarias
                if (dosis.isEmpty() || unidad.isEmpty() || frecuencia.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                    Toast.makeText(FormularioTratamientoActivity.this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Guardar los datos en la base de datos (implementación futura)
                    Toast.makeText(FormularioTratamientoActivity.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                    // Aquí puedes cerrar la actividad o hacer otras acciones necesarias
                    finish();
                }
            }
        });
    }
}
