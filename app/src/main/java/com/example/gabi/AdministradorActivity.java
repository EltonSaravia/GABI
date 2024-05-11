package com.example.gabi;
import com.example.gabi.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

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
            switch (item.getItemId()) {
                case R.id.homeAdministrador:
                    replaceFragment(new HomeAdministrador());
                    break;
                case R.id.empleados:
                    replaceFragment(new TrabajadoresAministrador());
                    break;
                case R.id.residentes:
                    replaceFragment(new ResidentesAdministrador());
                    break;
                case R.id.documentos:
                    replaceFragment(new DocumentosAdministrador());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
