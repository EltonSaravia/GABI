package com.example.gabi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
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
        View view = LayoutInflater.from(context).inflate(R.layout.card_view_mensajes, parent, false);
        return new HolderMensaje(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        holder.getNombre().setText(listMensajes.get(position).getNombre());
        holder.getMensaje().setText(listMensajes.get(position).getMensaje());
        holder.getHora().setText(listMensajes.get(position).getHora());

        // Cargar la imagen del perfil usando Glide
        Glide.with(context).load(listMensajes.get(position).getFotoPerfil()).into(holder.getFotoPerfil());
    }

    @Override
    public int getItemCount() {
        return listMensajes.size();
    }

    public class HolderMensaje extends RecyclerView.ViewHolder {

        private TextView nombre;
        private TextView mensaje;
        private TextView hora;
        private ImageView fotoPerfil;

        public HolderMensaje(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreMensaje);
            mensaje = itemView.findViewById(R.id.mensajeMensaje);
            hora = itemView.findViewById(R.id.horaMensaje);
            fotoPerfil = itemView.findViewById(R.id.fotoPerfilMensaje);
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

        public ImageView getFotoPerfil() {
            return fotoPerfil;
        }
    }
}
