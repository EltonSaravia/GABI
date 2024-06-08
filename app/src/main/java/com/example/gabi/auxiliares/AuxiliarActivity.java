package com.example.gabi.auxiliares;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.gabi.ChatActivity;
import com.example.gabi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.Toast;

public class AuxiliarActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auxiliar);

        // Solicitar permisos para enviar SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }

        String nombreUsuario = getIntent().getStringExtra("nombre");

        replaceFragment(new HomeAuxiliarFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeAuxiliar) {
                replaceFragment(new HomeAuxiliarFragment());
            } else if (id == R.id.turnosAuxiliar) {
                replaceFragment(new TurnosAuxiliarFragment());
            } else if (id == R.id.tareasAuxiliar) {
                replaceFragment(new TareasAuxiliarFragment());
            } else if (id == R.id.informacionAuxiliar) {
                replaceFragment(new InformacionAuxiliarFragment());
            }
            return true;
        });

        FloatingActionButton fabAbrirChat = findViewById(R.id.fabAbrirChat);
        fabAbrirChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("nombre", nombreUsuario); // Pasa el nombre del usuario al ChatActivity
            startActivity(intent);
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso para enviar SMS concedido de emergencia", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso para enviar SMS denegado de emergencia", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
