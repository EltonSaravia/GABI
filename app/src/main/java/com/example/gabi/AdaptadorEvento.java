package com.example.gabi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dto.EventoDTO;

public class AdaptadorEvento extends RecyclerView.Adapter<AdaptadorEvento.ViewHolder> {

    private List<EventoDTO> eventos;
    private Context context;

    public AdaptadorEvento(Context context, List<EventoDTO> eventos) {
        this.context = context;
        this.eventos = eventos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventoDTO evento = eventos.get(position);
        holder.tvNombre.setText(evento.getNombreResidente());
        holder.tvHora.setText(evento.getHoraEvento());
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvHora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreEvento);
            tvHora = itemView.findViewById(R.id.tvHoraEvento);
        }
    }
}
