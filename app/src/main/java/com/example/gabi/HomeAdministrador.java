package com.example.gabi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dto.EventoDTO;
import dto.TrabajadorDTO;
import dto.TurnoDTO;
import managers.EventosManager;

public class HomeAdministrador extends Fragment {

    private RecyclerView rvTrabajadores, rvEventos, rvTurnos;
    private AdaptadorTrabajador adaptadorTrabajador;
    private AdaptadorEvento adaptadorEvento;
    private adapters.AdaptadorTurno adaptadorTurno;
    private List<TrabajadorDTO> listaTrabajadores;
    private List<EventoDTO> listaEventos;
    private List<TurnoDTO> listaTurnos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_administrador, container, false);

        rvTrabajadores = view.findViewById(R.id.recyclerViewCurrentWorkers);
        rvEventos = view.findViewById(R.id.recyclerViewEvents);
        rvTurnos = view.findViewById(R.id.recyclerViewShifts);

        rvTrabajadores.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEventos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTurnos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Supongamos que tienes un manager para cada tipo de datos
        com.example.gabi.managers.TrabajadorManager trabajadorManager = new com.example.gabi.managers.TrabajadorManager(getContext());
        trabajadorManager.obtenerTrabajadoresEnJornada();

        EventosManager eventosManager = new EventosManager(getContext());
        listaEventos = eventosManager.obtenerEventosDelDia(); // Usar la instancia para llamar al método

        // Suponer que tienes una función para obtener los turnos
        listaTurnos = obtenerTurnosDelDia(); // Asegúrate de implementar este método

        // Inicializar los adaptadores
        adaptadorTrabajador = new AdaptadorTrabajador(getContext(), listaTrabajadores);
        adaptadorEvento = new AdaptadorEvento(getContext(), listaEventos);
        adaptadorTurno = new adapters.AdaptadorTurno(getContext(), listaTurnos);

        // Asignar los adaptadores a los RecyclerViews
        rvTrabajadores.setAdapter(adaptadorTrabajador);
        rvEventos.setAdapter(adaptadorEvento);
        rvTurnos.setAdapter(adaptadorTurno);

        return view;
    }
}
