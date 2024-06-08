package com.example.gabi.administrador.documentacion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.gabi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.*;

public class PagosFragment extends Fragment {

    private RecyclerView recyclerViewPagos;
    private PagoAdapter pagoAdapter;
    private List<PagoDTO> pagoList;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pagos, container, false);

        recyclerViewPagos = view.findViewById(R.id.recyclerViewPagos);
        recyclerViewPagos.setLayoutManager(new LinearLayoutManager(getContext()));
        pagoList = new ArrayList<>();
        pagoAdapter = new PagoAdapter(pagoList);
        recyclerViewPagos.setAdapter(pagoAdapter);

        requestQueue = Volley.newRequestQueue(getContext());

        obtenerPagos();

        return view;
    }

    private void obtenerPagos() {
        String url = "https://residencialontananza.com/api/estadoPagosResidentes.php";

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", "");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        if (status.equals("success")) {
                            JSONArray dataArray = jsonResponse.getJSONArray("data");
                            pagoList.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                PagoDTO pago = new PagoDTO(
                                        dataObject.getInt("id"),
                                        dataObject.getInt("residente_id"),
                                        dataObject.getString("fecha_entrada"),
                                        dataObject.optString("fecha_fin", null),
                                        dataObject.getString("fecha_corresponde_pago"),
                                        dataObject.getInt("estado_impago") == 1
                                );
                                pagoList.add(pago);
                            }
                            pagoAdapter.notifyDataSetChanged();
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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }
}
