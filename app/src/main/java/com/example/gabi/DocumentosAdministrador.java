package com.example.gabi;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.administrador.DocumentoAdapter;
import com.example.gabi.administrador.InputStreamVolleyRequest;
import com.example.gabi.administrador.documentacion.SubirArchivoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.DocumentoDTO;

public class DocumentosAdministrador extends Fragment implements DocumentoAdapter.OnDocumentoEliminarListener, DocumentoAdapter.OnDocumentoDescargarListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private EditText etBuscarDNI;
    private Button btnBuscar;
    private RecyclerView recyclerViewDocumentos;
    private DocumentoAdapter documentoAdapter;
    private List<DocumentoDTO> documentoList;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documentos_administrador, container, false);

        etBuscarDNI = view.findViewById(R.id.etBuscarDNI);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        recyclerViewDocumentos = view.findViewById(R.id.recyclerViewDocumentos);
        FloatingActionButton fabSubirArchivo = view.findViewById(R.id.fabSubirArchivo);

        recyclerViewDocumentos.setLayoutManager(new LinearLayoutManager(getContext()));
        documentoList = new ArrayList<>();
        documentoAdapter = new DocumentoAdapter(documentoList, this, this);
        recyclerViewDocumentos.setAdapter(documentoAdapter);

        requestQueue = Volley.newRequestQueue(getContext());

        btnBuscar.setOnClickListener(v -> buscarDocumentos());

        fabSubirArchivo.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new SubirArchivoFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Solicitar permisos de escritura si no están concedidos
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

        return view;
    }

    private void buscarDocumentos() {
        String dni = etBuscarDNI.getText().toString().trim();
        if (!dni.isEmpty()) {
            String url = "https://residencialontananza.com/api/listarDocumentos.php";

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
            final String token = sharedPreferences.getString("token", "");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            if (status.equals("success")) {
                                JSONArray dataArray = jsonResponse.getJSONArray("data");
                                documentoList.clear();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    DocumentoDTO documento = new DocumentoDTO(
                                            dataObject.getInt("id"),
                                            dataObject.getString("titulo"),
                                            dataObject.getString("descripcion"),
                                            dataObject.getString("nombre_archivo"),
                                            dataObject.getString("tipo_archivo"),
                                            dataObject.getString("fecha_subida"),
                                            new byte[0]
                                    );
                                    documentoList.add(documento);
                                }
                                documentoAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("dni", dni);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(getContext(), "Ingrese un DNI", Toast.LENGTH_SHORT).show();
        }
    }

    private DocumentoDTO findDocumentoById(int id) {
        for (DocumentoDTO documento : documentoList) {
            if (documento.getId() == id) {
                return documento;
            }
        }
        return null;
    }

    @Override
    public void onDocumentoEliminar(int id) {
        // Lógica para eliminar documento
    }

    @Override
    public void onDocumentoDescargar(int documentoId) {
        String url = "https://residencialontananza.com/api/descargarDocumento.php";

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", "");

        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(documentoId));

        InputStreamVolleyRequest inputStreamVolleyRequest = new InputStreamVolleyRequest(Request.Method.POST, url,
                response -> {
                    try {
                        if (response != null) {
                            DocumentoDTO documento = findDocumentoById(documentoId);
                            if (documento == null) {
                                Toast.makeText(getContext(), "Documento no encontrado", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String nombreArchivo = documento.getNombreArchivo();
                            String tipoArchivo = documento.getTipoArchivo();

                            // Save the file to Downloads directory
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            File file = new File(path, nombreArchivo);

                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(response);
                            fos.close();

                            Toast.makeText(getContext(), "Documento descargado correctamente", Toast.LENGTH_SHORT).show();

                            // Notify the system about the new download
                            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            downloadManager.addCompletedDownload(file.getName(), file.getName(), true, tipoArchivo, file.getAbsolutePath(), file.length(), true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error al guardar el archivo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }, params) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(inputStreamVolleyRequest);
    }
}
