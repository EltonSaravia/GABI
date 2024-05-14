package com.example.gabi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabi.R;

import java.util.List;

import dto.EventoDTO;

public class AdaptadorEvento extends RecyclerView.Adapter<AdaptadorEvento.ViewHolder> {

    private List<EventoDTO> eventos;
    private LayoutInflater inflater;

    public AdaptadorEvento(Context context, List<EventoDTO> eventos) {
        this.inflater = LayoutInflater.from(context);
        this.eventos = eventos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_evento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventoDTO evento = eventos.get(position);
        holder.bind(evento);
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

        void bind(EventoDTO evento) {
            tvNombre.setText(evento.getLugarCita());  // Asumiendo que "lugar_cita" es equivalente a "nombre del evento"
            tvHora.setText(evento.getHoraCita().toString());
        }
    }
}
