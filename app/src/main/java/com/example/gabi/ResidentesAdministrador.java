package com.example.gabi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gabi.R;
import com.example.gabi.administrador.residente.AgregarResidenteFragment;

public class ResidentesAdministrador extends Fragment {

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

        btnAgregar.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new AgregarResidenteFragment())
                    .addToBackStack(null)
                    .commit();
        });

        /*btnListar.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new ListarResidentesFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnModificar.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new ModificarResidenteFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnEliminar.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new EliminarResidenteFragment())
                    .addToBackStack(null)
                    .commit();
        });*/

        return view;
    }
}
