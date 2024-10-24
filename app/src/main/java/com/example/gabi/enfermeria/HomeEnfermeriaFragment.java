package com.example.gabi.enfermeria;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeEnfermeriaFragment extends Fragment {

    private RecyclerView recyclerViewAdministraciones;
    private AdministracionAdapter adapter;
    private List<Administracion> administracionList;
    private FloatingActionButton fabAgregarAdministracion;

    public HomeEnfermeriaFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_home_enfermeria, container, false);

        // Inicializar RecyclerView
        recyclerViewAdministraciones = view.findViewById(R.id.recyclerAdministraciones);
        recyclerViewAdministraciones.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar FloatingActionButton
        fabAgregarAdministracion = view.findViewById(R.id.fabAgregarAdministracion);

        // Manejar clic en el botón para agregar una nueva administración
        fabAgregarAdministracion.setOnClickListener(v -> {
            // Iniciar la actividad para el formulario de tratamiento
            Intent intent = new Intent(getContext(), FormularioTratamientoActivity.class);
            startActivity(intent);
        });

        // Lista de administraciones simulada
        administracionList = new ArrayList<>();
        administracionList.add(new Administracion("Residente 1", "Paracetamol", "Enfermero A", "2024-10-21 08:00", "500mg"));
        administracionList.add(new Administracion("Residente 2", "Ibuprofeno", "Enfermero B", "2024-10-21 09:00", "400mg"));

        // Configurar el adaptador
        adapter = new AdministracionAdapter(administracionList, administracion -> {
            // Crear un Intent para abrir la actividad de detalles
            Intent intent = new Intent(getContext(), DetalleAdministracionActivity.class);
            intent.putExtra("residente", administracion.getResidente());
            intent.putExtra("medicamento", administracion.getMedicamento());
            intent.putExtra("trabajador", administracion.getTrabajador());
            intent.putExtra("fechaHora", administracion.getFechaHora());
            intent.putExtra("dosis", administracion.getDosis());

            // Iniciar la actividad de detalles
            startActivity(intent);
        });

        recyclerViewAdministraciones.setAdapter(adapter);

        return view;
    }
}
