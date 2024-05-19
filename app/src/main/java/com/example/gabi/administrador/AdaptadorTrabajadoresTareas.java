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

public class AdaptadorTrabajadoresTareas extends RecyclerView.Adapter<AdaptadorTrabajadoresTareas.ViewHolder> {

    private Context context;
    private List<TrabajadorTurnoDTO> trabajadoresList;
    private OnItemClickListener onItemClickListener;

    public AdaptadorTrabajadoresTareas(Context context, List<TrabajadorTurnoDTO> trabajadoresList) {
        this.context = context;
        this.trabajadoresList = trabajadoresList;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_trabajador_asignar_tarea, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrabajadorTurnoDTO trabajador = trabajadoresList.get(position);

        holder.nombreTextView.setText(trabajador.getNombre());
        holder.apellido1TextView.setText(trabajador.getApellido1());
        holder.apellido2TextView.setText(trabajador.getApellido2());
        holder.puestoTextView.setText(trabajador.getPuesto());
        holder.turnoTextView.setText(trabajador.getTurno());

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
        return trabajadoresList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView apellido1TextView;
        TextView apellido2TextView;
        TextView puestoTextView;
        TextView turnoTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            apellido1TextView = itemView.findViewById(R.id.apellido1TextView);
            apellido2TextView = itemView.findViewById(R.id.apellido2TextView);
            puestoTextView = itemView.findViewById(R.id.puestoTextView);
            turnoTextView = itemView.findViewById(R.id.turnoTextView);
        }
    }
}
