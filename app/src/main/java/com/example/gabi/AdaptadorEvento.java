package com.example.gabi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dto.EventoDTO;

public class AdaptadorEvento extends RecyclerView.Adapter<AdaptadorEvento.EventoViewHolder> {
    private Context context;
    private List<EventoDTO> eventos;
    private int expandedPosition = -1;

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
    public void onBindViewHolder(final EventoViewHolder holder, int position) {
        EventoDTO evento = eventos.get(position);
        holder.tvHoraEvento.setText(evento.getHoraCita().toString());
        holder.tvMotivoEvento.setText(evento.getMotivoCita());
        holder.tvLugarEvento.setText(evento.getLugarCita());
        holder.tvDetallesEvento.setText(evento.getDetalles());

        // Mostrar el nombre y apellidos del residente
        holder.tvNombreResidente.setText(evento.getNombreResidente() + " " + evento.getApellidosResidente());

        final boolean isExpanded = holder.getAdapterPosition() == expandedPosition;
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedPosition = isExpanded ? -1 : holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoraEvento, tvMotivoEvento, tvLugarEvento, tvDetallesEvento, tvNombreResidente;
        LinearLayout expandableLayout;

        public EventoViewHolder(View itemView) {
            super(itemView);
            tvHoraEvento = itemView.findViewById(R.id.tvHoraEvento);
            tvMotivoEvento = itemView.findViewById(R.id.tvMotivoEvento);
            tvLugarEvento = itemView.findViewById(R.id.tvLugarEvento);
            tvDetallesEvento = itemView.findViewById(R.id.tvDetallesEvento);
            tvNombreResidente = itemView.findViewById(R.id.tvNombreResidente);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
        }
    }
}
