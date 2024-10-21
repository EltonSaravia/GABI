package com.example.gabi.enfermeria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class TurnosEnfermeriaFragment extends Fragment {

    public TurnosEnfermeriaFragment() {
        // Requiere un constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_turnos_enfermeria, container, false);
        TextView textView = view.findViewById(R.id.textViewTurnos);
        textView.setText("Aquí puedes gestionar los turnos");
        return view;
    }
}
