package com.example.gabi.auxiliares;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.MainActivity;
import com.example.gabi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeAuxiliarFragment extends Fragment {

    private Button btnRegistrarEntrada;
    private Button btnRegistrarSalida;
    private Button btnLogoutAuxiliar;
    private String token;
    private int trabajadorId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_auxiliar, container, false);

        btnRegistrarEntrada = view.findViewById(R.id.btnRegistrarEntrada);
        btnRegistrarSalida = view.findViewById(R.id.btnRegistrarSalida);
        btnLogoutAuxiliar = view.findViewById(R.id.btnLogoutAuxiliar);

        // Obtener el token y el ID del trabajador de SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getActivity().MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        trabajadorId = sharedPreferences.getInt("trabajador_id", -1); // Asegúrate de guardar el ID del trabajador en SharedPreferences

        btnRegistrarEntrada.setOnClickListener(v -> mostrarConfirmacionEntrada());
        btnRegistrarSalida.setOnClickListener(v -> mostrarConfirmacionSalida());
        btnLogoutAuxiliar.setOnClickListener(v -> mostrarDialogoLogout());

        return view;
    }

    private void mostrarConfirmacionEntrada() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmar Entrada")
                .setMessage("¿Estás seguro de que deseas registrar tu entrada?")
                .setPositiveButton("Sí", (dialog, which) -> registrarEntrada())
                .setNegativeButton("No", null)
                .show();
    }

    private void mostrarConfirmacionSalida() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmar Salida");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Añadir nota (opcional)");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Sí", (dialog, which) -> registrarSalida(input.getText().toString()));
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void registrarEntrada() {
        String url = "https://residencialontananza.com/aux/registrarEntrada.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            Toast.makeText(getActivity(), "Entrada registrada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Error en la respuesta del servidor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getActivity(), "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("trabajador_id", String.valueOf(trabajadorId));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    private void registrarSalida(String notas) {
        String url = "https://residencialontananza.com/aux/registrarSalida.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            Toast.makeText(getActivity(), "Salida registrada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Error en la respuesta del servidor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getActivity(), "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("trabajador_id", String.valueOf(trabajadorId));
                params.put("notas", notas);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    private void mostrarDialogoLogout() {
        // Crear y mostrar un diálogo de confirmación
        new AlertDialog.Builder(getContext())
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Borrar el token y navegar a la pantalla de login
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("token");
                    editor.apply();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
