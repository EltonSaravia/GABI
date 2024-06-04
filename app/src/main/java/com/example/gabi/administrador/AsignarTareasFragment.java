package com.example.gabi.administrador;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import com.example.gabi.R;
import java.util.Calendar;
import java.util.List;
import dto.TrabajadorTurnoDTO;
import managers.TrabajadorManager;

public class AsignarTareasFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button btnBuscarTrabajadores;
    private DatePicker datePicker;
    private List<TrabajadorTurnoDTO> listaTrabajadores;

    public AsignarTareasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignar_tareas, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTrabajadores);
        btnBuscarTrabajadores = view.findViewById(R.id.btnBuscarTrabajadores);
        datePicker = view.findViewById(R.id.datePicker);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnBuscarTrabajadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarTrabajadoresPorFecha();
            }
        });

        return view;
    }

    private void buscarTrabajadoresPorFecha() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        String fechaSeleccionada = year + "-" + (month + 1) + "-" + day;

        TrabajadorManager trabajadorManager = new TrabajadorManager(getContext());
        trabajadorManager.obtenerTrabajadoresPorFecha(fechaSeleccionada, new TrabajadorManager.TrabajadorTurnoCallback() {
            @Override
            public void onSuccess(List<TrabajadorTurnoDTO> trabajadores) {
                listaTrabajadores = trabajadores;
                AdaptadorTrabajadoresTareas adaptador = new AdaptadorTrabajadoresTareas(getContext(), listaTrabajadores);
                adaptador.setOnItemClickListener(new AdaptadorTrabajadoresTareas.OnItemClickListener() {
                    @Override
                    public void onItemClick(TrabajadorTurnoDTO trabajador) {
                        asignarTarea(trabajador, fechaSeleccionada);
                    }
                });
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error al cargar trabajadores: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asignarTarea(TrabajadorTurnoDTO trabajador, String fechaSeleccionada) {
        AsignarTareasEmpleadoFragment fragment = new AsignarTareasEmpleadoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("trabajador", trabajador);
        bundle.putString("fechaSeleccionada", fechaSeleccionada);
        fragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
