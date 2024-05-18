package com.example.gabi.administrador;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.R;
import managers.TrabajadorManager;
import dto.TrabajadorDTO;
import managers.TrabajadorCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActualizarTrabajadorFragment extends Fragment {

    private EditText txtDNI, txtNombre, txtApellido1, txtApellido2, txtTelefono, txtEmail, txtContrasena;
    private Spinner spnPuesto;
    private Button btnBuscar, btnActualizar, btnCancelar;
    private int trabajadorId; // ID del trabajador buscado
    private String token;

    public ActualizarTrabajadorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actualizar_trabajador, container, false);

        // Asociar los controles
        txtDNI = view.findViewById(R.id.dni);
        txtNombre = view.findViewById(R.id.nombre);
        txtApellido1 = view.findViewById(R.id.apellido1);
        txtApellido2 = view.findViewById(R.id.apellido2);
        txtTelefono = view.findViewById(R.id.telefono);
        txtEmail = view.findViewById(R.id.email);
        txtContrasena = view.findViewById(R.id.contrasena);
        spnPuesto = view.findViewById(R.id.spinnerPuesto);
        btnBuscar = view.findViewById(R.id.botonBuscar);
        btnActualizar = view.findViewById(R.id.botonActualizar);
        btnCancelar = view.findViewById(R.id.botonCancelar);

        // Obtener el token desde SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        // Configurar el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.puestos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPuesto.setAdapter(adapter);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarTrabajador();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarTrabajador();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void buscarTrabajador() {
        final String dni = txtDNI.getText().toString().trim();

        if (dni.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingrese el DNI", Toast.LENGTH_SHORT).show();
            return;
        }

        TrabajadorManager trabajadorManager = new TrabajadorManager(getContext());
        trabajadorManager.buscarTrabajador(dni, token, new TrabajadorCallback() {
            @Override
            public void onSuccess(List<TrabajadorDTO> trabajadores) {
                if (!trabajadores.isEmpty()) {
                    TrabajadorDTO trabajador = trabajadores.get(0);
                    trabajadorId = trabajador.getId(); // Guardar el ID del trabajador
                    txtNombre.setText(trabajador.getNombre());
                    txtApellido1.setText(trabajador.getApellido1());
                    txtApellido2.setText(trabajador.getApellido2());
                    txtTelefono.setText(trabajador.getTelefono());
                    txtEmail.setText(trabajador.getEmail());

                    // Set the spinner position to match the puesto
                    String puesto = trabajador.getPuesto();
                    ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spnPuesto.getAdapter();
                    if (adapter != null) {
                        int spinnerPosition = adapter.getPosition(puesto);
                        spnPuesto.setSelection(spinnerPosition);
                    }
                } else {
                    Toast.makeText(getContext(), "Trabajador no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error al buscar trabajador: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarTrabajador() {
        final String dni = txtDNI.getText().toString().trim();
        final String nombre = txtNombre.getText().toString().trim();
        final String apellido1 = txtApellido1.getText().toString().trim();
        final String apellido2 = txtApellido2.getText().toString().trim();
        final String telefono = txtTelefono.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String contrasena = txtContrasena.getText().toString().trim();
        final String puesto = spnPuesto.getSelectedItem().toString();

        if (dni.isEmpty() || nombre.isEmpty() || apellido1.isEmpty() || apellido2.isEmpty() || telefono.isEmpty() || email.isEmpty() || contrasena.isEmpty() || puesto.isEmpty()) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, "https://residencialontananza.com/api/actualizarDatosTrabajador.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("success")) {
                                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error de conexión: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(trabajadorId));
                params.put("dni", dni);
                params.put("nombre", nombre);
                params.put("apellido1", apellido1);
                params.put("apellido2", apellido2);
                params.put("telefono", telefono);
                params.put("email", email);
                params.put("contrasena", contrasena);
                params.put("puesto", puesto);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
