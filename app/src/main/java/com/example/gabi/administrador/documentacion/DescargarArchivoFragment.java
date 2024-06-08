package com.example.gabi.administrador.documentacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gabi.R;

public class DescargarArchivoFragment extends Fragment {

    private EditText etBusqueda;
    private Button btnBuscar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descargar_archivo, container, false);

        etBusqueda = view.findViewById(R.id.etBusqueda);
        btnBuscar = view.findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(v -> buscarYDescargarDocumentos(etBusqueda.getText().toString()));

        return view;
    }

    private void buscarYDescargarDocumentos(String query) {

    }
}
