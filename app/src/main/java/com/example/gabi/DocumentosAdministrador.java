package com.example.gabi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.gabi.administrador.documentacion.DescargarArchivoFragment;
import com.example.gabi.administrador.documentacion.SubirArchivoFragment;

public class DocumentosAdministrador extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documentos_administrador, container, false);

        Button btnSubirArchivo = view.findViewById(R.id.btnSubirArchivo);
        Button btnDescargarArchivo = view.findViewById(R.id.btnDescargarArchivo);

        btnSubirArchivo.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new SubirArchivoFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnDescargarArchivo.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new DescargarArchivoFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
