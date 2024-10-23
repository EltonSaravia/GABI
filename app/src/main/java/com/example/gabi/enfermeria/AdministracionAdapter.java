package com.example.gabi.enfermeria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabi.R;

import java.util.List;

public class AdministracionAdapter extends RecyclerView.Adapter<AdministracionAdapter.AdministracionViewHolder> {

    // Interfaz para manejar el clic en los elementos
    public interface OnItemClickListener {
        void onItemClick(Administracion administracion);
    }

    private List<Administracion> administracionList;
    private OnItemClickListener listener;

    // Constructor para recibir la lista de administraciones y el listener para clics
    public AdministracionAdapter(List<Administracion> administracionList, OnItemClickListener listener) {
        this.administracionList = administracionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdministracionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del elemento de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_administracion, parent, false);
        return new AdministracionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdministracionViewHolder holder, int position) {
        // Asignar los valores de la administración actual al ViewHolder
        Administracion administracion = administracionList.get(position);
        holder.bind(administracion, listener);
    }

    @Override
    public int getItemCount() {
        // Retorna el tamaño de la lista
        return administracionList.size();
    }

    // ViewHolder que se encarga de cada item en el RecyclerView
    public static class AdministracionViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewResidente;
        private TextView textViewMedicamento;
        private TextView textViewFechaHora;

        public AdministracionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asignar las vistas del layout item_administracion
            textViewResidente = itemView.findViewById(R.id.textViewResidente);
            textViewMedicamento = itemView.findViewById(R.id.textViewMedicamento);
            textViewFechaHora = itemView.findViewById(R.id.textViewFechaHora);
        }

        // Método para enlazar los datos de la administración al ViewHolder
        public void bind(final Administracion administracion, final OnItemClickListener listener) {
            textViewResidente.setText(administracion.getResidente());
            textViewMedicamento.setText(administracion.getMedicamento());
            textViewFechaHora.setText(administracion.getFechaHora());

            // Hacer el elemento clicable
            itemView.setOnClickListener(v -> listener.onItemClick(administracion));
        }
    }
}
