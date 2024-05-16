package com.example.gabi.administrador;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gabi.R;
import com.example.gabi.AdaptadorTrabajador;
import managers.TrabajadorManager;
import managers.TrabajadorCallback;
import dto.TrabajadorDTO;
import java.util.List;


public class ListarTrabajadoresFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdaptadorTrabajador adaptadorTrabajador;

    public ListarTrabajadoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar_trabajadores, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTrabajadores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarTrabajadores();

        return view;
    }

    private void cargarTrabajadores() {
        TrabajadorManager manager = new TrabajadorManager(getContext());
        manager.obtenerTrabajadoresEnJornada(new TrabajadorCallback() {
            @Override
            public void onSuccess(List<TrabajadorDTO> trabajadores) {
                adaptadorTrabajador = new AdaptadorTrabajador(getContext(), trabajadores);
                recyclerView.setAdapter(adaptadorTrabajador);
            }

            @Override
            public void onError(String error) {
                // Manejar error
            }
        });
    }
}