package com.example.gabi.administrador.documentacion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SubirArchivoFragment extends Fragment {

    private static final String TAG = "SubirArchivoFragment";
    private Uri fileUri;
    private String fileName;
    private EditText etDNI, etTitulo, etDescripcion;
    private TextView tvNombreArchivo;
    private Button btnSeleccionarArchivo, btnSubirArchivo;

    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subir_archivo, container, false);

        etDNI = view.findViewById(R.id.etDNI);
        etTitulo = view.findViewById(R.id.etTitulo);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        btnSeleccionarArchivo = view.findViewById(R.id.btnSeleccionarArchivo);
        btnSubirArchivo = view.findViewById(R.id.btnSubirArchivo);
        tvNombreArchivo = view.findViewById(R.id.tvNombreArchivo);

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        fileUri = result.getData().getData();
                        if (fileUri != null) {
                            fileName = getFileName(fileUri);
                            tvNombreArchivo.setText(fileName);
                            Toast.makeText(getContext(), "Archivo seleccionado: " + fileName, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Archivo seleccionado: " + fileName);
                        } else {
                            Log.e(TAG, "fileUri es nulo");
                        }
                    } else {
                        Log.e(TAG, "Error al seleccionar archivo");
                    }
                }
        );

        btnSeleccionarArchivo.setOnClickListener(v -> openFileChooser());

        btnSubirArchivo.setOnClickListener(v -> uploadFile());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Seleccionar Archivo"));
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void uploadFile() {
        final String dni = etDNI.getText().toString().trim();
        final String titulo = etTitulo.getText().toString().trim();
        final String descripcion = etDescripcion.getText().toString().trim();

        Log.d(TAG, "DNI: " + dni);
        Log.d(TAG, "Título: " + titulo);
        Log.d(TAG, "Descripción: " + descripcion);
        Log.d(TAG, "fileUri: " + fileUri);
        Log.d(TAG, "fileName: " + fileName);

        if (dni.isEmpty() || titulo.isEmpty() || descripcion.isEmpty() || fileUri == null || fileName == null) {
            Toast.makeText(getContext(), "Faltan datos obligatorios o archivo no seleccionado.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, "https://residencialontananza.com/api/subirDocumento.php",
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(getContext(), "Documento subido correctamente.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error al procesar la respuesta del servidor.", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error al subir documento: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("dni", dni);
                params.put("titulo", titulo);
                params.put("descripcion", descripcion);
                params.put("nombreArchivo", fileName);

                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(fileUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                    inputStream.close();
                    String encodedFile = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                    params.put("documento", encodedFile);
                    Log.d(TAG, "Archivo codificado en base64: " + encodedFile.substring(0, 30) + "...");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error al leer el archivo: " + e.getMessage());
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
