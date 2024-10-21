package com.example.gabi.enfermeria;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gabi.ChatActivity;
import com.example.gabi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EnfermeriaActivity extends AppCompatActivity {

  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enfermeria);

        String nombreUsuario = getIntent().getStringExtra("nombre");

        replaceFragment(new HomeEnfermeriaFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeEnfermeria) {
                replaceFragment(new HomeEnfermeriaFragment());
            } else if (id == R.id.turnosEnfermeria) {
                replaceFragment(new TurnosEnfermeriaFragment());
            } else if (id == R.id.tareasEnfermeria) {
                replaceFragment(new TareasEnfermeriaFragment());
            } else if (id == R.id.informacionEnfermeria) {
                replaceFragment(new InformacionEnfermeriaFragment());
            }
            return true;
        });

        FloatingActionButton fabAbrirChat = findViewById(R.id.fabAbrirChat);
        fabAbrirChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("nombre", nombreUsuario);
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
