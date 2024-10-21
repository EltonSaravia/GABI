package com.example.gabi.enfermeria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.gabi.R;

public class HomeEnfermeriaFragment extends Fragment {

    public HomeEnfermeriaFragment() {
        // Requiere un constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_home_enfermeria, container, false);
        TextView textView = view.findViewById(R.id.textViewHome);
        textView.setText("Bienvenido a la sección de Enfermería");
        return view;
    }
}
