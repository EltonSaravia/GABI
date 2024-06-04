package com.example.gabi.auxiliares;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gabi.R;

import java.util.List;

import dto.TareaDTO;
import managers.TareaCallback;
import managers.TareaManager;

public class TareasAuxiliarFragment extends Fragment {

    private RecyclerView recyclerViewTareas;
    private TareaAdapterAuxiliar tareaAdapter;
    private TareaManager tareaManager;

    public TareasAuxiliarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tareas_auxiliar, container, false);

        recyclerViewTareas = view.findViewById(R.id.recyclerViewTareas);
        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        int trabajadorId = sharedPreferences.getInt("trabajador_id", -1);

        tareaManager = new TareaManager(getContext(), token);
        tareaManager.obtenerTareasParaAuxiliar(trabajadorId, new TareaCallback() {
            @Override
            public void onSuccess(List<TareaDTO> tareaList) {
                tareaAdapter = new TareaAdapterAuxiliar(tareaList, getContext(), tareaManager);
                recyclerViewTareas.setAdapter(tareaAdapter);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error al obtener tareas: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String message) {
                // Este método no se utiliza en este fragmento
            }
        });

        return view;
    }
}
