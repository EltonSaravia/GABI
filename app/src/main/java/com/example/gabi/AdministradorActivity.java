package com.example.gabi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.gabi.databinding.ActivityAdministradorBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdministradorActivity extends AppCompatActivity {

    private ActivityAdministradorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdministradorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String nombreUsuario = getIntent().getStringExtra("nombre");

        replaceFragment(new HomeAdministrador());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeAdministrador) {
                replaceFragment(new HomeAdministrador());
            } else if (id == R.id.empleadosAdministrador) {
                replaceFragment(new TrabajadoresAministrador());
            } else if (id == R.id.residentesAdministrador) {
                replaceFragment(new ResidentesAdministrador());
            } else if (id == R.id.documentosAdministrador) {
                replaceFragment(new DocumentosAdministrador());
            } else if (id == R.id.chatAdministrador) {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("nombre", nombreUsuario); // Pasa el nombre del usuario al ChatActivity
                startActivity(intent);
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
}
