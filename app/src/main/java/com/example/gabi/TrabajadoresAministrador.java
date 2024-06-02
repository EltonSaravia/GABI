package com.example.gabi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.gabi.administrador.EliminarTrabajadorFragment;
import com.example.gabi.administrador.ListarTrabajadoresFragment;
import com.example.gabi.administrador.ActualizarTrabajadorFragment;
import com.example.gabi.administrador.AsignarTareasFragment;
import com.example.gabi.administrador.AsignarTurnosFragment;
import com.example.gabi.administrador.*;

public class TrabajadoresAministrador extends Fragment {

    public TrabajadoresAministrador() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trabajadores_administrador, container, false);

        FrameLayout btnCrear = view.findViewById(R.id.btnCrearTrabajador);
        FrameLayout btnListar = view.findViewById(R.id.btnListarTrabajadores);
        FrameLayout btnActualizar = view.findViewById(R.id.btnActualizarTrabajador);
        FrameLayout btnEliminar = view.findViewById(R.id.btnEliminarTrabajador);
        FrameLayout btnAsignarTareas = view.findViewById(R.id.btnAsignarTareas);
        FrameLayout btnAsignarTurnos = view.findViewById(R.id.btnAsignarTurnos);

        Button btnCrearInner = view.findViewById(R.id.btnCrearTrabajadorInner);
        Button btnListarInner = view.findViewById(R.id.btnListarTrabajadoresInner);
        Button btnActualizarInner = view.findViewById(R.id.btnActualizarTrabajadorInner);
        Button btnEliminarInner = view.findViewById(R.id.btnEliminarTrabajadorInner);
        Button btnAsignarTareasInner = view.findViewById(R.id.btnAsignarTareasInner);
        Button btnAsignarTurnosInner = view.findViewById(R.id.btnAsignarTurnosInner);

        View.OnClickListener crearListener = v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new RegistrarTrabajadorFragment())
                    .addToBackStack(null)
                    .commit();
        };

        View.OnClickListener listarListener = v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new ListarTrabajadoresFragment())
                    .addToBackStack(null)
                    .commit();
        };

        View.OnClickListener actualizarListener = v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new ActualizarTrabajadorFragment())
                    .addToBackStack(null)
                    .commit();
        };

        View.OnClickListener eliminarListener = v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new EliminarTrabajadorFragment())
                    .addToBackStack(null)
                    .commit();
        };

        View.OnClickListener asignarTareasListener = v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new AsignarTareasFragment())
                    .addToBackStack(null)
                    .commit();
        };

        View.OnClickListener asignarTurnosListener = v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new AsignarTurnosFragment())
                    .addToBackStack(null)
                    .commit();
        };

        btnCrear.setOnClickListener(crearListener);
        btnCrearInner.setOnClickListener(crearListener);

        btnListar.setOnClickListener(listarListener);
        btnListarInner.setOnClickListener(listarListener);

        btnActualizar.setOnClickListener(actualizarListener);
        btnActualizarInner.setOnClickListener(actualizarListener);

        btnEliminar.setOnClickListener(eliminarListener);
        btnEliminarInner.setOnClickListener(eliminarListener);

        btnAsignarTareas.setOnClickListener(asignarTareasListener);
        btnAsignarTareasInner.setOnClickListener(asignarTareasListener);

        btnAsignarTurnos.setOnClickListener(asignarTurnosListener);
        btnAsignarTurnosInner.setOnClickListener(asignarTurnosListener);

        return view;
    }
}
