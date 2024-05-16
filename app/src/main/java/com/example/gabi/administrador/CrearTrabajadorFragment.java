package com.example.gabi.administrador;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;
import java.util.Map;

public class CrearTrabajadorFragment extends Fragment {

    EditText txtDNI, txtNombre, txtApellido1, txtApellido2, txtTelefono, txtEmail, txtContrasena;
    Spinner spnPuesto;
    Button btnRegistrar, btnCancelar;

    public CrearTrabajadorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.formulario_usuario, container, false);

        // Asociar los controles
        txtDNI = view.findViewById(R.id.dni);
        txtNombre = view.findViewById(R.id.nombre);
        txtApellido1 = view.findViewById(R.id.apellido1);
        txtApellido2 = view.findViewById(R.id.apellido2);
        txtTelefono = view.findViewById(R.id.telefono);
        txtEmail = view.findViewById(R.id.email);
        txtContrasena = view.findViewById(R.id.contrasena);
        spnPuesto = view.findViewById(R.id.spinnerPuesto);
        btnRegistrar = view.findViewById(R.id.botonRegistrar);
        btnCancelar = view.findViewById(R.id.botonCancelar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarTrabajador();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código para manejar la cancelación, por ejemplo, volver a la lista de trabajadores
            }
        });

        return view;
    }

    private void registrarTrabajador() {
        final String dni = txtDNI.getText().toString().trim();
        final String nombre = txtNombre.getText().toString().trim();
        final String apellido1 = txtApellido1.getText().toString().trim();
        final String apellido2 = txtApellido2.getText().toString().trim();
        final String telefono = txtTelefono.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String contrasena = txtContrasena.getText().toString().trim();
        final String puesto = spnPuesto.getSelectedItem().toString();

        // Validar campos vacíos
        if (dni.isEmpty() || nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(getContext(), "Faltan campos por llenar", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, "https://residencialontananza.com/public_html/api/insertarUsuario.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
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
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
