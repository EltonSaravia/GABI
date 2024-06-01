package com.example.gabi.auxiliares;

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

public class AuxiliarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auxiliar);

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
}
