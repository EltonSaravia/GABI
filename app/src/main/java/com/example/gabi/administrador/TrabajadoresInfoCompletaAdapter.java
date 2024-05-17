package com.example.gabi.administrador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gabi.R;
import java.util.List;
import dto.TrabajadorDTO;

public class TrabajadoresInfoCompletaAdapter extends RecyclerView.Adapter<TrabajadoresInfoCompletaAdapter.ViewHolder> {

    private List<TrabajadorDTO> listaTrabajadores;

    public TrabajadoresInfoCompletaAdapter(List<TrabajadorDTO> listaTrabajadores) {
        this.listaTrabajadores = listaTrabajadores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trabajador_info_completa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrabajadorDTO trabajador = listaTrabajadores.get(position);
        holder.txtNombre.setText(trabajador.getNombre());
        holder.txtApellido1.setText(trabajador.getApellido1());
        holder.txtApellido2.setText(trabajador.getApellido2());
        holder.txtPuesto.setText(trabajador.getPuesto());
        holder.txtTelefono.setText(trabajador.getTelefono());
        holder.txtEmail.setText(trabajador.getEmail());
    }

    @Override
    public int getItemCount() {
        return listaTrabajadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtApellido1, txtApellido2, txtPuesto, txtTelefono, txtEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtApellido1 = itemView.findViewById(R.id.txtApellido1);
            txtApellido2 = itemView.findViewById(R.id.txtApellido2);
            txtPuesto = itemView.findViewById(R.id.txtPuesto);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);
            txtEmail = itemView.findViewById(R.id.txtEmail);
        }
    }
}
