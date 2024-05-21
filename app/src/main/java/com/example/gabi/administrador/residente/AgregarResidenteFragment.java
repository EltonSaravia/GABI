package com.example.gabi.administrador.residente;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gabi.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AgregarResidenteFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText txtDNI, txtNombre, txtApellido1, txtApellido2, txtFechaNacimiento, txtTelefono, txtEmail, txtObservaciones, txtAR, txtNSS, txtNumeroCuentaBancaria, txtEmpadronamiento;
    Button btnRegistrar, btnCancelar, btnSeleccionarFoto;
    ImageView imageViewFoto;

    private Bitmap selectedBitmap;

    public AgregarResidenteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agregar_residente, container, false);

        // Asociar los controles
        txtDNI = view.findViewById(R.id.dni);
        txtNombre = view.findViewById(R.id.nombre);
        txtApellido1 = view.findViewById(R.id.apellido1);
        txtApellido2 = view.findViewById(R.id.apellido2);
        txtFechaNacimiento = view.findViewById(R.id.fecha_nacimiento);
        txtTelefono = view.findViewById(R.id.telefono);
        txtEmail = view.findViewById(R.id.email);
        txtObservaciones = view.findViewById(R.id.observaciones);
        txtAR = view.findViewById(R.id.ar);
        txtNSS = view.findViewById(R.id.nss);
        txtNumeroCuentaBancaria = view.findViewById(R.id.numero_cuenta_bancaria);
        txtEmpadronamiento = view.findViewById(R.id.empadronamiento);
        btnRegistrar = view.findViewById(R.id.botonRegistrar);
        btnCancelar = view.findViewById(R.id.botonCancelar);
        btnSeleccionarFoto = view.findViewById(R.id.btnSeleccionarFoto);
        imageViewFoto = view.findViewById(R.id.imageViewFoto);

        txtFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarResidente();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código para manejar la cancelación, por ejemplo, volver a la lista de residentes
            }
        });

        return view;
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        txtFechaNacimiento.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageViewFoto.setImageBitmap(selectedBitmap);
                imageViewFoto.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registrarResidente() {
        final String dni = txtDNI.getText().toString().trim();
        final String nombre = txtNombre.getText().toString().trim();
        final String apellido1 = txtApellido1.getText().toString().trim();
        final String apellido2 = txtApellido2.getText().toString().trim();
        final String fechaNacimiento = txtFechaNacimiento.getText().toString().trim();
        final String telefono = txtTelefono.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String observaciones = txtObservaciones.getText().toString().trim();
        final String ar = txtAR.getText().toString().trim();
        final String nss = txtNSS.getText().toString().trim();
        final String numeroCuentaBancaria = txtNumeroCuentaBancaria.getText().toString().trim();
        final String empadronamiento = txtEmpadronamiento.getText().toString().trim();

        // Validar campos vacíos
        if (dni.isEmpty() || nombre.isEmpty() || apellido1.isEmpty() || apellido2.isEmpty() || ar.isEmpty() || nss.isEmpty() || numeroCuentaBancaria.isEmpty() || empadronamiento.isEmpty()) {
            Toast.makeText(getContext(), "Faltan campos por llenar", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, "https://residencialontananza.com/api/insertarResidente.php",
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("dni", dni);
                params.put("nombre", nombre);
                params.put("apellido1", apellido1);
                params.put("apellido2", apellido2);
                params.put("fecha_nacimiento", fechaNacimiento);
                params.put("telefono", telefono);
                params.put("email", email);
                params.put("observaciones", observaciones);
                params.put("ar", ar);
                params.put("nss", nss);
                params.put("numero_cuenta_bancaria", numeroCuentaBancaria);
                params.put("empadronamiento", empadronamiento);
                params.put("foto", encodeImageToBase64(selectedBitmap));
                params.put("estado", "1"); // Estado true
                params.put("activo", "1"); // Activo true
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
