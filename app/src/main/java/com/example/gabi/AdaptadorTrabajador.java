package com.example.gabi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dto.TrabajadorDTO;

public class AdaptadorTrabajador extends RecyclerView.Adapter<AdaptadorTrabajador.ViewHolder> {

    private List<TrabajadorDTO> listaTrabajadores;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private int selectedPosition = RecyclerView.NO_POSITION; // Variable para almacenar la posición seleccionada

    public AdaptadorTrabajador(Context context, List<TrabajadorDTO> listaTrabajadores) {
        this.inflater = LayoutInflater.from(context);
        this.listaTrabajadores = listaTrabajadores;
    }

    public interface OnItemClickListener {
        void onItemClick(TrabajadorDTO trabajador);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_trabajador, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrabajadorDTO trabajador = listaTrabajadores.get(position);
        holder.bind(trabajador);

        // Cambiar el fondo del ítem según si está seleccionado o no
        if (selectedPosition == holder.getAdapterPosition()) {
            holder.itemView.setBackgroundColor(Color.LTGRAY); // Color de fondo seleccionado
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE); // Color de fondo no seleccionado
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(trabajador);
            }
            // Actualizar la posición seleccionada
            int previousSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return listaTrabajadores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtApellido1, txtApellido2, txtPuesto;

        ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtApellido1 = itemView.findViewById(R.id.txtApellido1);
            txtApellido2 = itemView.findViewById(R.id.txtApellido2);
            txtPuesto = itemView.findViewById(R.id.txtPuesto);
        }

        void bind(TrabajadorDTO trabajador) {
            txtNombre.setText(trabajador.getNombre());
            txtApellido1.setText(trabajador.getApellido1());
            txtApellido2.setText(trabajador.getApellido2());
            txtPuesto.setText(trabajador.getPuesto());
        }
    }
}
