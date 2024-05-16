package com.example.gabi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import managers.EventosCallback;
import managers.EventosManager;
import managers.TrabajadorCallback;
import managers.TrabajadorManager;
import managers.TurnoCallback;
import managers.TurnoManager;

public class HomeAdministrador extends Fragment {

    private RecyclerView rvTrabajadores, rvEventos, rvTurnos;
    private AdaptadorTrabajador adaptadorTrabajador;
    private AdaptadorEvento adaptadorEvento;
    private AdaptadorTurno adaptadorTurno;
    private List<TrabajadorDTO> listaTrabajadores;
    private List<EventoDTO> listaEventos;
    private List<TurnoDTO> listaTurnos;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_administrador, container, false);

        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        rvTrabajadores = view.findViewById(R.id.recyclerViewCurrentWorkers);
        rvEventos = view.findViewById(R.id.recyclerViewEvents);
        rvTurnos = view.findViewById(R.id.recyclerViewShifts);

        rvTrabajadores.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEventos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTurnos.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarTrabajadores();
        cargarEventos();
        cargarTurnos();

        return view;
    }

    private void cargarTrabajadores() {
        TrabajadorManager trabajadorManager = new TrabajadorManager(getContext());
        trabajadorManager.obtenerTrabajadoresEnJornada(new TrabajadorCallback() {
            @Override
            public void onSuccess(List<TrabajadorDTO> trabajadores) {
                listaTrabajadores = trabajadores;
                adaptadorTrabajador = new AdaptadorTrabajador(getContext(), listaTrabajadores);
                rvTrabajadores.setAdapter(adaptadorTrabajador);
                adaptadorTrabajador.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Log.e("TrabajadorError", "Error: " + error);
            }
        });
    }

    private void cargarEventos() {
        EventosManager eventosManager = new EventosManager(getContext());
        eventosManager.obtenerEventosDelDia(new EventosCallback() {
            @Override
            public void onSuccess(List<EventoDTO> eventos) {
                listaEventos = eventos;
                adaptadorEvento = new AdaptadorEvento(getContext(), listaEventos);
                rvEventos.setAdapter(adaptadorEvento);
                adaptadorEvento.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Log.e("EventosError", "Error: " + error);
            }
        });
    }

    private void cargarTurnos() {
        TurnoManager turnoManager = new TurnoManager(getContext(), token); // Pasar el token aquí
        Log.d("HomeAdministrador", "Token: " + token); // Log del token

        turnoManager.obtenerTurnosDiaActual(new TurnoCallback() {
            @Override
            public void onSuccess(List<TurnoDTO> turnos) {
                Log.d("HomeAdministrador", "Received turnos: " + turnos.size());
                for (TurnoDTO turno : turnos) {
                    Log.d("HomeAdministrador", "Turno: " + turno.getNombre() + " " + turno.getApellido1() + " " + turno.getApellido2());
                }
                listaTurnos = turnos;
                adaptadorTurno = new AdaptadorTurno(getContext(), listaTurnos);
                rvTurnos.setAdapter(adaptadorTurno);
                adaptadorTurno.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Log.e("TurnoError", "Error: " + error);
            }
        });
    }
}
