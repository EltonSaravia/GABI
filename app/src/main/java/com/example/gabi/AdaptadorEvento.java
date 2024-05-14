package com.example.gabi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dto.EventoDTO;

public class AdaptadorEvento extends RecyclerView.Adapter<AdaptadorEvento.EventoViewHolder> {

    private Context context;
    private List<EventoDTO> eventos;

    public AdaptadorEvento(Context context, List<EventoDTO> eventos) {
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventoViewHolder holder, int position) {
        EventoDTO evento = eventos.get(position);
        holder.tvHoraEvento.setText(evento.getHoraCita().toString()); // Convertimos Time a String
        holder.tvMotivoEvento.setText(evento.getMotivoCita());
        holder.tvLugarEvento.setText(evento.getLugarCita());
        holder.tvDetallesEvento.setText(evento.getDetalles());
    }


    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoraEvento, tvMotivoEvento, tvLugarEvento, tvDetallesEvento;

        public EventoViewHolder(View itemView) {
            super(itemView);
            tvHoraEvento = itemView.findViewById(R.id.tvHoraEvento);
            tvMotivoEvento = itemView.findViewById(R.id.tvMotivoEvento);
            tvLugarEvento = itemView.findViewById(R.id.tvLugarEvento);
            tvDetallesEvento = itemView.findViewById(R.id.tvDetallesEvento);
        }
    }
}
