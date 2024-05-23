package com.example.gabi.administrador.residente;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gabi.R;
import dto.ResidenteDTO;
import java.util.List;

public class ResidenteAdapter extends RecyclerView.Adapter<ResidenteAdapter.ResidenteViewHolder> {

    private List<ResidenteDTO> residentes;

    public ResidenteAdapter(List<ResidenteDTO> residentes) {
        this.residentes = residentes;
    }

    @NonNull
    @Override
    public ResidenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_residente, parent, false);
        return new ResidenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResidenteViewHolder holder, int position) {
        ResidenteDTO residente = residentes.get(position);
        holder.nombre.setText(residente.getNombre());
        holder.dni.setText(residente.getDni());
        holder.telefono.setText(residente.getTelefono());
        holder.email.setText(residente.getEmail());

        // Cambiar el color de fondo y el color del texto basado en el estado activo
        if ("false".equals(residente.getActivo())) {
            holder.itemView.setBackgroundColor(Color.RED);
            holder.nombre.setTextColor(Color.WHITE);
            holder.dni.setTextColor(Color.WHITE);
            holder.telefono.setTextColor(Color.WHITE);
            holder.email.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.nombre.setTextColor(Color.BLACK);
            holder.dni.setTextColor(Color.BLACK);
            holder.telefono.setTextColor(Color.BLACK);
            holder.email.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return residentes.size();
    }

    static class ResidenteViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, dni, telefono, email;

        ResidenteViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            dni = itemView.findViewById(R.id.dni);
            telefono = itemView.findViewById(R.id.telefono);
            email = itemView.findViewById(R.id.email);
        }
    }
}
