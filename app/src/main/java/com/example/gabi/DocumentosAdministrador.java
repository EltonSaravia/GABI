package com.example.gabi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.administrador.DocumentoAdapter;
import com.example.gabi.administrador.documentacion.SubirArchivoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.DocumentoDTO;

public class DocumentosAdministrador extends Fragment implements DocumentoAdapter.OnDocumentoEliminarListener, DocumentoAdapter.OnDocumentoDescargarListener {

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

    @Override
    public void onDocumentoEliminar(int id) {
        // Lógica para eliminar documento
    }

    @Override
    public void onDocumentoDescargar(int id) {
        // Lógica para descargar documento
    }
}
