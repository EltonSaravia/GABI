package com.example.gabi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.example.gabi.AdaptadorEvento;
import com.example.gabi.AdaptadorTrabajador;
import com.example.gabi.AdaptadorTurno;
import com.example.gabi.*;


import dto.EventoDTO;
import dto.TrabajadorDTO;
import dto.TurnoDTO;
import managers.EventosManager;
import managers.TrabajadorManager;
import managers.TurnoManager;

public class HomeAdministrador extends Fragment {

    private RecyclerView rvTrabajadores, rvEventos, rvTurnos;
    private AdaptadorTrabajador adaptadorTrabajador;
    private AdaptadorEvento adaptadorEvento;
    private AdaptadorTurno adaptadorTurno;
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

        TrabajadorManager trabajadorManager = new TrabajadorManager(getContext());
        trabajadorManager.obtenerTrabajadoresEnJornada(new TrabajadorManager.TrabajadorCallback() {
            @Override
            public void onSuccess(List<TrabajadorDTO> trabajadores) {
                listaTrabajadores = trabajadores;
                adaptadorTrabajador = new AdaptadorTrabajador(getContext(), listaTrabajadores);
                rvTrabajadores.setAdapter(adaptadorTrabajador);
            }

            @Override
            public void onError(String error) {
                // Manejar error
            }
        });

        EventosManager eventosManager = new EventosManager(getContext());
        eventosManager.obtenerEventosDelDia(new EventosManager.EventoCallback() {
            @Override
            public void onSuccess(List<EventoDTO> eventos) {
                listaEventos = eventos;
                adaptadorEvento = new AdaptadorEvento(getContext(), listaEventos);
                rvEventos.setAdapter(adaptadorEvento);
            }

            @Override
            public void onError(String error) {
                // Manejar error
            }
        });

        TurnoManager turnoManager = new TurnoManager(getContext());
        turnoManager.obtenerTurnosDelDia(new TurnoManager.TurnoCallback() {
            @Override
            public void onSuccess(List<TurnoDTO> turnos) {
                listaTurnos = turnos;
                adaptadorTurno = new AdaptadorTurno(getContext(), listaTurnos);
                rvTurnos.setAdapter(adaptadorTurno);
            }

            @Override
            public void onError(String error) {
                // Manejar error
            }
        });

        return view;
    }
}
