package com.example.gabi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.gabi.administrador.EliminarTrabajadorFragment;
import com.example.gabi.administrador.ListarTrabajadoresFragment;
import com.example.gabi.administrador.ActualizarTrabajadorFragment;
import com.example.gabi.administrador.AsignarTareasFragment;
import com.example.gabi.administrador.AsignarTurnosFragment;

public class TrabajadoresAministrador extends Fragment {

    public TrabajadoresAministrador() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trabajadores_administrador, container, false);

        Button btnCrear = view.findViewById(R.id.btnCrearTrabajador);
        Button btnListar = view.findViewById(R.id.btnListarTrabajadores);
        Button btnActualizar = view.findViewById(R.id.btnActualizarTrabajador);
        Button btnEliminar = view.findViewById(R.id.btnEliminarTrabajador);
        Button btnAsignarTareas = view.findViewById(R.id.btnAsignarTareas);
        Button btnAsignarTurnos = view.findViewById(R.id.btnAsignarTurnos);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new RegistrarTrabajadorFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new ListarTrabajadoresFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new ActualizarTrabajadorFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new EliminarTrabajadorFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnAsignarTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new AsignarTareasFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnAsignarTurnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new AsignarTurnosFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}
