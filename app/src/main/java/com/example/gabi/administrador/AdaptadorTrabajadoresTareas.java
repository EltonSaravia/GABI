package com.example.gabi.administrador;

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

public class AdaptadorTrabajadoresTareas extends RecyclerView.Adapter<AdaptadorTrabajadoresTareas.ViewHolder> {

    private List<TrabajadorDTO> listaTrabajadores;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public AdaptadorTrabajadoresTareas(Context context, List<TrabajadorDTO> listaTrabajadores) {
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
        holder.txtNombre.setText(trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());
        holder.txtPuesto.setText(trabajador.getPuesto());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(trabajador);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTrabajadores.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(TrabajadorDTO trabajador);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPuesto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPuesto = itemView.findViewById(R.id.txtPuesto);
        }
    }
}
