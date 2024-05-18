package com.example.gabi.administrador;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gabi.R;
import dto.TrabajadorDTO;

public class AsignarTareasEmpleadoFragment extends Fragment {

    private TextView textViewNombreEmpleado;
    private EditText editTextTarea;
    private TimePicker timePickerHora;
    private Button btnAsignarTarea;
    private TrabajadorDTO trabajador;

    public AsignarTareasEmpleadoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignar_tareas_empleado, container, false);

        textViewNombreEmpleado = view.findViewById(R.id.textViewNombreEmpleado);
        editTextTarea = view.findViewById(R.id.editTextTarea);
        timePickerHora = view.findViewById(R.id.timePickerHora);
        btnAsignarTarea = view.findViewById(R.id.btnAsignarTarea);

        // Obtener el trabajador del Bundle
        if (getArguments() != null) {
            trabajador = (TrabajadorDTO) getArguments().getSerializable("trabajador");
            textViewNombreEmpleado.setText(trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());
        }

        btnAsignarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignarTarea();
            }
        });

        return view;
    }

    private void asignarTarea() {
        String descripcionTarea = editTextTarea.getText().toString();
        int hora = timePickerHora.getHour();
        int minuto = timePickerHora.getMinute();

        // Aquí puedes agregar la lógica para guardar la tarea en la base de datos
        // ...

        Toast.makeText(getContext(), "Tarea asignada: " + descripcionTarea + " a las " + hora + ":" + minuto, Toast.LENGTH_SHORT).show();
    }
}
