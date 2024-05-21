package com.example.gabi.administrador;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import com.example.gabi.R;
import java.util.List;
import dto.TrabajadorTurnoDTO;
import managers.TrabajadorManager;

public class AsignarTurnosFragment extends Fragment {

    private DatePicker datePicker;
    private Button btnBuscarTrabajadores, btnAsignarNuevoTurno;
    private RecyclerView recyclerViewTrabajadores;
    private String fechaSeleccionada;

    public AsignarTurnosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignar_turnos, container, false);

        datePicker = view.findViewById(R.id.datePicker);
        btnBuscarTrabajadores = view.findViewById(R.id.btnBuscarTrabajadores);
        btnAsignarNuevoTurno = view.findViewById(R.id.btnAsignarNuevoTurno);
        recyclerViewTrabajadores = view.findViewById(R.id.recyclerViewTrabajadores);

        btnBuscarTrabajadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarTrabajadores();
            }
        });

        btnAsignarNuevoTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("fechaSeleccionada", fechaSeleccionada); // Pasar la fecha seleccionada al siguiente fragmento
                AsignarTurnosEmpleadoFragment fragment = new AsignarTurnosEmpleadoFragment();
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void buscarTrabajadores() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, day);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        TrabajadorManager trabajadorManager = new TrabajadorManager(getContext());
        trabajadorManager.obtenerTrabajadoresPorFecha(fechaSeleccionada, new TrabajadorManager.TrabajadorTurnoCallback() {
            @Override
            public void onSuccess(List<TrabajadorTurnoDTO> trabajadorList) {
                AdaptadorTrabajadorTurnoAsignar adaptador = new AdaptadorTrabajadorTurnoAsignar(getContext(), trabajadorList);
                adaptador.setOnItemClickListener(new AdaptadorTrabajadorTurnoAsignar.OnItemClickListener() {
                    @Override
                    public void onItemClick(TrabajadorTurnoDTO trabajador) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("trabajador", trabajador);
                        bundle.putString("fechaSeleccionada", fechaSeleccionada); // Pasar la fecha seleccionada al siguiente fragmento
                        AsignarTurnosEmpleadoFragment fragment = new AsignarTurnosEmpleadoFragment();
                        fragment.setArguments(bundle);

                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
                recyclerViewTrabajadores.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewTrabajadores.setAdapter(adaptador);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error al buscar trabajadores: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
