package com.example.gabi.administrador;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.gabi.R;
import dto.TrabajadorTurnoDTO;
import managers.TareaManager;

public class AsignarTareasEmpleadoFragment extends Fragment {

    private TextView textViewNombreEmpleado;
    private Spinner spinnerTituloTarea;
    private EditText editTextNotas;
    private TimePicker timePickerHora;
    private Button btnAsignarTarea;
    private TrabajadorTurnoDTO trabajador;

    public AsignarTareasEmpleadoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignar_tareas_empleado, container, false);

        textViewNombreEmpleado = view.findViewById(R.id.textViewNombreEmpleado);
        spinnerTituloTarea = view.findViewById(R.id.spinnerTituloTarea);
        editTextNotas = view.findViewById(R.id.editTextNotas);
        timePickerHora = view.findViewById(R.id.timePickerHora);
        btnAsignarTarea = view.findViewById(R.id.btnAsignarTarea);

        // Configurar el Spinner con las tareas
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tareas_auxiliar, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTituloTarea.setAdapter(adapter);

        // Obtener el trabajador del Bundle
        if (getArguments() != null) {
            trabajador = (TrabajadorTurnoDTO) getArguments().getSerializable("trabajador");
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
        String tituloTarea = spinnerTituloTarea.getSelectedItem().toString();
        String notas = editTextNotas.getText().toString();
        int hora = timePickerHora.getHour();
        int minuto = timePickerHora.getMinute();
        String horaTareaAsignada = String.format("%02d:%02d:00", hora, minuto);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        TareaManager tareaManager = new TareaManager(getContext(), token);
        tareaManager.asignarTarea(trabajador.getId(), tituloTarea, notas, "2024-05-19", horaTareaAsignada, new TareaManager.TareaCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Tarea asignada: " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error al asignar tarea: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
