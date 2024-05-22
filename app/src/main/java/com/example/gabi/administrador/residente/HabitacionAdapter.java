package com.example.gabi.administrador.residente;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gabi.R;
import dto.HabitacionDTO;
import java.util.List;

public class HabitacionAdapter extends RecyclerView.Adapter<HabitacionAdapter.HabitacionViewHolder> {

    private List<HabitacionDTO> habitaciones;
    private OnItemClickListener listener;
    private int selectedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(HabitacionDTO habitacion);
    }

    public HabitacionAdapter(List<HabitacionDTO> habitaciones, OnItemClickListener listener) {
        this.habitaciones = habitaciones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habitacion, parent, false);
        return new HabitacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitacionViewHolder holder, int position) {
        HabitacionDTO habitacion = habitaciones.get(position);
        holder.bind(habitacion, listener, position);
    }

    @Override
    public int getItemCount() {
        return habitaciones.size();
    }

    public class HabitacionViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNumeroHabitacion, txtPisoHabitacion, txtObservaciones;
        private CardView cardView;

        public HabitacionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumeroHabitacion = itemView.findViewById(R.id.txtNumeroHabitacion);
            txtPisoHabitacion = itemView.findViewById(R.id.txtPisoHabitacion);
            txtObservaciones = itemView.findViewById(R.id.txtObservaciones);
            cardView = itemView.findViewById(R.id.cardView);
        }

        public void bind(final HabitacionDTO habitacion, final OnItemClickListener listener, final int position) {
            txtNumeroHabitacion.setText("Número: " + habitacion.getNumero());
            txtPisoHabitacion.setText("Piso: " + habitacion.getPiso());
            txtObservaciones.setText("Observaciones: " + habitacion.getObservaciones());

            if (habitacion.isOcupada()) {
                cardView.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.rojo));
            } else {
                cardView.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.verde));
            }

            if (position == selectedPosition) {
                cardView.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.amarillo));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(habitacion);
                    notifyItemChanged(selectedPosition);
                    selectedPosition = getAdapterPosition();
                    notifyItemChanged(selectedPosition);
                }
            });
        }
    }
}
