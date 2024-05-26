package com.example.gabi.administrador.documentacion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gabi.R;

public class SubirArchivoFragment extends Fragment {

    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;
    private EditText etDNI, etTitulo, etDescripcion;
    private Button btnSeleccionarArchivo, btnSubirArchivo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subir_archivo, container, false);

        etDNI = view.findViewById(R.id.etDNI);
        etTitulo = view.findViewById(R.id.etTitulo);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        btnSeleccionarArchivo = view.findViewById(R.id.btnSeleccionarArchivo);
        btnSubirArchivo = view.findViewById(R.id.btnSubirArchivo);

        btnSeleccionarArchivo.setOnClickListener(v -> openFileChooser());
        btnSubirArchivo.setOnClickListener(v -> uploadFile());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
        }
    }

    private void uploadFile() {
        // Lógica para subir el archivo al servidor
        // Puedes usar la lógica proporcionada en la respuesta anterior
    }
}
