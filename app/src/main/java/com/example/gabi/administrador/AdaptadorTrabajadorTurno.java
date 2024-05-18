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
import dto.TrabajadorTurnoDTO;

public class AdaptadorTrabajadorTurno extends RecyclerView.Adapter<AdaptadorTrabajadorTurno.ViewHolder> {

    private Context context;
    private List<TrabajadorTurnoDTO> listaTrabajadores;

    public AdaptadorTrabajadorTurno(Context context, List<TrabajadorTurnoDTO> listaTrabajadores) {
        this.context = context;
        this.listaTrabajadores = listaTrabajadores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trabajador_turno, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrabajadorTurnoDTO trabajador = listaTrabajadores.get(position);
        holder.txtNombre.setText(trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());
        holder.txtPuesto.setText(trabajador.getPuesto());
        holder.txtTurno.setText(trabajador.getTurno());
    }

    @Override
    public int getItemCount() {
        return listaTrabajadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPuesto, txtTurno;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPuesto = itemView.findViewById(R.id.txtPuesto);
            txtTurno = itemView.findViewById(R.id.txtTurno);
        }
    }
}
