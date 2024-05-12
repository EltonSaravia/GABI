package com.example.gabi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dto.TrabajadorDTO;

public class AdaptadorTrabajador extends RecyclerView.Adapter<AdaptadorTrabajador.ViewHolder> {

    private List<TrabajadorDTO> trabajadores;
    private Context context;

    public AdaptadorTrabajador(Context context, List<TrabajadorDTO> trabajadores) {
        this.context = context;
        this.trabajadores = trabajadores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trabajador, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrabajadorDTO trabajador = trabajadores.get(position);
        holder.tvNombre.setText(trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());
        holder.tvPuesto.setText(trabajador.getPuesto());
    }

    @Override
    public int getItemCount() {
        return trabajadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPuesto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreTrabajador);
            tvPuesto = itemView.findViewById(R.id.tvPuestoTrabajador);
        }
    }
}
