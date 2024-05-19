package com.example.gabi.administrador;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gabi.R;

import java.util.List;

import dto.TrabajadorTurnoDTO;
import dto.TurnoDTO;
import managers.TurnoManager;
import managers.TurnoCallback;

public class AsignarTurnosEmpleadoFragment extends Fragment {

    private TextView textViewNombreEmpleado;
    private RadioGroup radioGroupTurno;
    private RadioButton radioButtonDiurno, radioButtonVespertino, radioButtonNocturno;
    private Button btnAsignarTurno;
    private TrabajadorTurnoDTO trabajador;
    private String fechaSeleccionada; // Nueva variable para almacenar la fecha seleccionada

    public AsignarTurnosEmpleadoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignar_turnos_empleado, container, false);

        textViewNombreEmpleado = view.findViewById(R.id.textViewNombreEmpleado);
        radioGroupTurno = view.findViewById(R.id.radioGroupTurno);
        radioButtonDiurno = view.findViewById(R.id.radioButtonDiurno);
        radioButtonVespertino = view.findViewById(R.id.radioButtonVespertino);
        radioButtonNocturno = view.findViewById(R.id.radioButtonNocturno);
        btnAsignarTurno = view.findViewById(R.id.btnAsignarTurno);

        if (getArguments() != null) {
            trabajador = (TrabajadorTurnoDTO) getArguments().getSerializable("trabajador");
            fechaSeleccionada = getArguments().getString("fechaSeleccionada"); // Obtener la fecha seleccionada del bundle
            textViewNombreEmpleado.setText(trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());
        }

        btnAsignarTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignarTurno();
            }
        });

        return view;
    }

    private void asignarTurno() {
        String turno = "";
        int selectedId = radioGroupTurno.getCheckedRadioButtonId();
        if (selectedId == radioButtonDiurno.getId()) {
            turno = "diurno";
        } else if (selectedId == radioButtonVespertino.getId()) {
            turno = "vespertino";
        } else if (selectedId == radioButtonNocturno.getId()) {
            turno = "nocturno";
        }

        if (turno.isEmpty()) {
            Toast.makeText(getContext(), "Por favor seleccione un turno", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        TurnoManager turnoManager = new TurnoManager(getContext(), token);
        turnoManager.asignarTurno(trabajador.getId(), turno, fechaSeleccionada, new TurnoCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Turno asignado: " + message, Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error al asignar turno: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<TurnoDTO> turnos) {
                // No se usa aquí
            }
        });
    }
}
