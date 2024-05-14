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

import dto.TrabajadorDTO;

public class AdaptadorTrabajador extends RecyclerView.Adapter<AdaptadorTrabajador.ViewHolder> {

    private List<TrabajadorDTO> listaTrabajadores;
    private LayoutInflater inflater;

    // Constructor
    public AdaptadorTrabajador(Context context, List<TrabajadorDTO> listaTrabajadores) {
        this.inflater = LayoutInflater.from(context);
        this.listaTrabajadores = listaTrabajadores;
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
    }

    @Override
    public int getItemCount() {
        return listaTrabajadores.size();
    }

    // ViewHolder class
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
