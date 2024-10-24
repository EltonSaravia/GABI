package com.example.gabi.enfermeria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.gabi.R;

public class TareasEnfermeriaFragment extends Fragment {

    public TareasEnfermeriaFragment() {
        // Requiere un constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_tareas_enfermeria, container, false);
        TextView textView = view.findViewById(R.id.textViewTareas);
        textView.setText("Aquí puedes ver y gestionar las tareas");
        return view;
    }
}