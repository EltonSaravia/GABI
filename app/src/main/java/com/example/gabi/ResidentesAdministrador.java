package com.example.gabi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

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

        FrameLayout btnAgregar = view.findViewById(R.id.btnAgregarResidente);
        FrameLayout btnListar = view.findViewById(R.id.btnListarResidentes);
        FrameLayout btnModificar = view.findViewById(R.id.btnModificarResidente);
        FrameLayout btnAsignarHabitacion = view.findViewById(R.id.btnAsignarHabitacion);

        Button btnAgregarInner = view.findViewById(R.id.btnAgregarResidenteInner);
        Button btnListarInner = view.findViewById(R.id.btnListarResidentesInner);
        Button btnModificarInner = view.findViewById(R.id.btnModificarResidenteInner);
        Button btnAsignarHabitacionInner = view.findViewById(R.id.btnAsignarHabitacionInner);

        View.OnClickListener agregarListener = v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new AgregarResidenteFragment())
                    .addToBackStack(null)
                    .commit();
        };

        View.OnClickListener listarListener = v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new ListarResidentesFragment())
                    .addToBackStack(null)
                    .commit();
        };

        View.OnClickListener modificarListener = v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new ActualizarResidenteFragment())
                    .addToBackStack(null)
                    .commit();
        };

        View.OnClickListener asignarListener = v -> {
            AsignarHabitacionFragment fragment = new AsignarHabitacionFragment();
            Bundle args = new Bundle();
            args.putInt("residente_id", residenteId); // Pasar el ID del residente seleccionado
            fragment.setArguments(args);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        };

        btnAgregar.setOnClickListener(agregarListener);
        btnAgregarInner.setOnClickListener(agregarListener);

        btnListar.setOnClickListener(listarListener);
        btnListarInner.setOnClickListener(listarListener);

        btnModificar.setOnClickListener(modificarListener);
        btnModificarInner.setOnClickListener(modificarListener);

        btnAsignarHabitacion.setOnClickListener(asignarListener);
        btnAsignarHabitacionInner.setOnClickListener(asignarListener);

        return view;
    }
}
