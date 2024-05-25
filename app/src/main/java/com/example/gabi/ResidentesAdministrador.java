package com.example.gabi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gabi.administrador.residente.ActualizarResidenteFragment;
import com.example.gabi.administrador.residente.AgregarResidenteFragment;
import com.example.gabi.administrador.residente.AsignarHabitacionFragment;
import com.example.gabi.administrador.residente.ListarResidentesFragment;

public class ResidentesAdministrador extends Fragment {

    private int residenteId;

    public ResidentesAdministrador() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_residentes_administrador, container, false);

        Button btnAgregar = view.findViewById(R.id.btnAgregarResidente);
        Button btnListar = view.findViewById(R.id.btnListarResidentes);
        Button btnModificar = view.findViewById(R.id.btnModificarResidente);
        Button btnEliminar = view.findViewById(R.id.btnEliminarResidente);
        Button btnAsignarHabitacion = view.findViewById(R.id.btnAsignarHabitacion);

        btnAgregar.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new AgregarResidenteFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new ListarResidentesFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new ActualizarResidenteFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnAsignarHabitacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignarHabitacionFragment fragment = new AsignarHabitacionFragment();
                Bundle args = new Bundle();
                args.putInt("residente_id", residenteId); // Pasar el ID del residente seleccionado
                fragment.setArguments(args);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Deja los otros botones comentados si no se están utilizando ahora.
        return view;
    }
}
