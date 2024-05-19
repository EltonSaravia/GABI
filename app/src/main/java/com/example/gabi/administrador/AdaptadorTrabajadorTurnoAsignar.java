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

public class AdaptadorTrabajadorTurnoAsignar extends RecyclerView.Adapter<AdaptadorTrabajadorTurnoAsignar.ViewHolder> {

    private Context context;
    private List<TrabajadorTurnoDTO> listaTrabajadores;
    private OnItemClickListener onItemClickListener;

    public AdaptadorTrabajadorTurnoAsignar(Context context, List<TrabajadorTurnoDTO> listaTrabajadores) {
        this.context = context;
        this.listaTrabajadores = listaTrabajadores;
    }

    public interface OnItemClickListener {
        void onItemClick(TrabajadorTurnoDTO trabajador);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trabajador_turno_asignar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrabajadorTurnoDTO trabajador = listaTrabajadores.get(position);
        holder.txtNombre.setText(trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());
        holder.txtPuesto.setText(trabajador.getPuesto());
        holder.txtTurno.setText(trabajador.getTurno());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(trabajador);
                }
            }
        });
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
