package com.example.gabi;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gabi.R;

public class HolderMensaje extends RecyclerView.ViewHolder {
    private TextView nombre;
    private TextView mensaje;
    private TextView hora;
    private ImageView fotoMensajePerfil;

    public HolderMensaje(View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.nombreMensaje);
        mensaje = itemView.findViewById(R.id.mensajeMensaje);
        hora = itemView.findViewById(R.id.horaMensaje);
        fotoMensajePerfil = itemView.findViewById(R.id.fotoPerfilMensaje);
    }

    public TextView getNombre() {
        return nombre;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public TextView getHora() {
        return hora;
    }

    public ImageView getFotoMensajePerfil() {
        return fotoMensajePerfil;
    }
}
