package com.example.gabi.enfermeria;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gabi.R;

public class DetalleAdministracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_administracion);

        // Obtener los datos enviados desde HomeEnfermeriaFragment
        String residente = getIntent().getStringExtra("residente");
        String medicamento = getIntent().getStringExtra("medicamento");
        String trabajador = getIntent().getStringExtra("trabajador");
        String fechaHora = getIntent().getStringExtra("fechaHora");
        String dosis = getIntent().getStringExtra("dosis");

        // Enlazar los TextViews con los datos
        TextView textViewResidente = findViewById(R.id.textViewResidenteDetalle);
        TextView textViewMedicamento = findViewById(R.id.textViewMedicamentoDetalle);
        TextView textViewTrabajador = findViewById(R.id.textViewTrabajadorDetalle);
        TextView textViewFechaHora = findViewById(R.id.textViewFechaHoraDetalle);
        TextView textViewDosis = findViewById(R.id.textViewDosisDetalle);

        // Mostrar los datos en los TextViews
        textViewResidente.setText(residente);
        textViewMedicamento.setText(medicamento);
        textViewTrabajador.setText(trabajador);
        textViewFechaHora.setText(fechaHora);
        textViewDosis.setText(dosis);
    }
}
