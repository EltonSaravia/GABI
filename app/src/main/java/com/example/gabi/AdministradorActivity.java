package com.example.gabi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.gabi.databinding.ActivityAdministradorBinding;

public class AdministradorActivity extends AppCompatActivity {

    private ActivityAdministradorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdministradorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Carga el fragmento inicial
        replaceFragment(new HomeAdministrador());
        binding.bottomNavigationView.setBackground(null);

        // Configura el listener para la navegación
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
                startActivity(new Intent(this, ChatActivity.class));
            }
            return true;
        });

        // Configura el listener para el FloatingActionButton
        binding.fabAbrirChat.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
