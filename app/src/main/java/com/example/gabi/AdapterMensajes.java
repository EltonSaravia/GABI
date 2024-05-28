package com.example.gabi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<AdapterMensajes.HolderMensaje> {

    private List<MensajeRecibir> listMensajes = new ArrayList<>();
    private Context context;

    public AdapterMensajes(Context context) {
        this.context = context;
    }

    public void addMensaje(MensajeRecibir m) {
        listMensajes.add(m);
        notifyItemInserted(listMensajes.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_view_mensajes, parent, false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        MensajeRecibir mensaje = listMensajes.get(position);
        holder.nombre.setText(mensaje.getNombre());
        holder.mensaje.setText(mensaje.getMensaje());

        // Manejo de la hora del mensaje
        if (mensaje.getHora() != null) {
            Long codigoHora = mensaje.getHora();
            Date d = new Date(codigoHora);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a"); // a pm o am
            holder.hora.setText(sdf.format(d));
        } else {
            holder.hora.setText("Sin hora");
        }
    }

    @Override
    public int getItemCount() {
        return listMensajes.size();
    }

    public static class HolderMensaje extends RecyclerView.ViewHolder {
        private TextView nombre;
        private TextView mensaje;
        private TextView hora;

        public HolderMensaje(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreMensaje);
            mensaje = itemView.findViewById(R.id.mensajeMensaje);
            hora = itemView.findViewById(R.id.horaMensaje);
        }
    }
}
