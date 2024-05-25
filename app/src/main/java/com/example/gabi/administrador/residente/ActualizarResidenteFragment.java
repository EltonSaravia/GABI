package com.example.gabi.administrador.residente;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dto.ResidenteDTO;
import managers.ResidenteCallback;
import managers.ResidenteManager;

public class ActualizarResidenteFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    EditText txtDNI, txtNombre, txtApellidos, txtFechaNacimiento, txtTelefono, txtEmail, txtObservaciones, txtAR, txtNSS, txtNumeroCuentaBancaria, txtEmpadronamiento;
    Button btnBuscar, btnActualizar, btnCancelar, btnSeleccionarFoto;
    ImageView imageViewFoto;

    private Bitmap selectedBitmap;
    private int residenteId;
    private String token;

    public ActualizarResidenteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualizar_residente, container, false);

        // Asociar los controles
        txtDNI = view.findViewById(R.id.dni);
        txtNombre = view.findViewById(R.id.nombre);
        txtApellidos = view.findViewById(R.id.apellidos);
        txtFechaNacimiento = view.findViewById(R.id.fecha_nacimiento);
        txtTelefono = view.findViewById(R.id.telefono);
        txtEmail = view.findViewById(R.id.email);
        txtObservaciones = view.findViewById(R.id.observaciones);
        txtAR = view.findViewById(R.id.ar);
        txtNSS = view.findViewById(R.id.nss);
        txtNumeroCuentaBancaria = view.findViewById(R.id.numero_cuenta_bancaria);
        txtEmpadronamiento = view.findViewById(R.id.empadronamiento);
        btnBuscar = view.findViewById(R.id.botonBuscar);
        btnActualizar = view.findViewById(R.id.botonActualizar);
        btnCancelar = view.findViewById(R.id.botonCancelar);
        btnSeleccionarFoto = view.findViewById(R.id.btnSeleccionarFoto);
        imageViewFoto = view.findViewById(R.id.imageViewFoto);

        // Obtener el token desde SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        txtFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarResidente();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarResidente();
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

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                txtFechaNacimiento.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void buscarResidente() {
        final String dni = txtDNI.getText().toString().trim();

        if (dni.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingrese el DNI", Toast.LENGTH_SHORT).show();
            return;
        }

        ResidenteManager residenteManager = new ResidenteManager(getContext());
        residenteManager.buscarResidente(dni, token, new ResidenteCallback() {
            @Override
            public void onSuccess(ResidenteDTO residente) {
                if (residente != null) {
                    residenteId = residente.getId(); // Guardar el ID del residente
                    txtNombre.setText(residente.getNombre());
                    txtApellidos.setText(residente.getApellidos());
                    txtFechaNacimiento.setText(dateFormat.format(residente.getFechaNacimiento())); // Convertir Date a String
                    txtTelefono.setText(residente.getTelefono());
                    txtEmail.setText(residente.getEmail());
                    txtObservaciones.setText(residente.getObservaciones());
                    txtAR.setText(residente.getAr());
                    txtNSS.setText(residente.getNss());
                    txtNumeroCuentaBancaria.setText(residente.getNumeroCuentaBancaria());
                    txtEmpadronamiento.setText(residente.getEmpadronamiento());
                    // Asignar foto si existe
                    if (residente.getFoto() != null && residente.getFoto().length > 0) {
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(residente.getFoto(), 0, residente.getFoto().length);
                        imageViewFoto.setImageBitmap(decodedByte);
                        imageViewFoto.setVisibility(View.VISIBLE);
                    } else {
                        imageViewFoto.setImageResource(R.drawable.imagen_generica); // Usa la imagen genérica si no hay foto
                    }
                } else {
                    Toast.makeText(getContext(), "Residente no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error al buscar residente: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarResidente() {
        final String dni = txtDNI.getText().toString().trim();
        final String nombre = txtNombre.getText().toString().trim();
        final String apellidos = txtApellidos.getText().toString().trim();
        final String fechaNacimiento = txtFechaNacimiento.getText().toString().trim();
        final String telefono = txtTelefono.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String observaciones = txtObservaciones.getText().toString().trim();
        final String ar = txtAR.getText().toString().trim();
        final String nss = txtNSS.getText().toString().trim();
        final String numeroCuentaBancaria = txtNumeroCuentaBancaria.getText().toString().trim();
        final String empadronamiento = txtEmpadronamiento.getText().toString().trim();

        // Validar campos obligatorios
        if (dni.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || ar.isEmpty() || nss.isEmpty() || fechaNacimiento.isEmpty()) {
            Toast.makeText(getContext(), "Faltan campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, "https://residencialontananza.com/api/actualizarResidente.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_LONG).show();
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
                params.put("id", String.valueOf(residenteId));
                params.put("dni", dni);
                params.put("nombre", nombre);
                params.put("apellidos", apellidos); // Usar el campo combinado
                params.put("fecha_nacimiento", fechaNacimiento.isEmpty() ? "" : fechaNacimiento);
                params.put("telefono", telefono.isEmpty() ? "No hay" : telefono);
                params.put("email", email.isEmpty() ? "No hay" : email);
                params.put("observaciones", observaciones.isEmpty() ? "No hay" : observaciones);
                params.put("ar", ar.isEmpty() ? "" : ar);
                params.put("nss", nss.isEmpty() ? "" : nss);
                params.put("numero_cuenta_bancaria", numeroCuentaBancaria.isEmpty() ? "No hay" : numeroCuentaBancaria);
                params.put("empadronamiento", empadronamiento.isEmpty() ? "No hay" : empadronamiento);
                if (selectedBitmap != null) {
                    params.put("foto", encodeImageToBase64(selectedBitmap));
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
