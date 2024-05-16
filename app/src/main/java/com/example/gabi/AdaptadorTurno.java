package com.example.gabi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dto.TurnoDTO;
public class AdaptadorTurno extends RecyclerView.Adapter<AdaptadorTurno.TurnoViewHolder> {

    private List<TurnoDTO> listaTurnos;
    private Context context;

    public AdaptadorTurno(Context context, List<TurnoDTO> listaTurnos) {
        this.listaTurnos = listaTurnos;
        this.context = context;
    }

    @NonNull
    @Override
    public TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("AdaptadorTurno", "onCreateViewHolder called");
        View view = LayoutInflater.from(context).inflate(R.layout.item_turno, parent, false);
        return new TurnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
        Log.d("AdaptadorTurno", "onBindViewHolder called for position " + position);
        TurnoDTO turno = listaTurnos.get(position);
        holder.txtNombre.setText(turno.getNombre());
        holder.txtApellido1.setText(turno.getApellido1());
        holder.txtApellido2.setText(turno.getApellido2());
        holder.txtPuesto.setText(turno.getPuesto());

        // Cambiar el color de fondo según el tipo de turno
        int color;
        switch (turno.getTipo()) {
            case "diurno":
                color = context.getResources().getColor(R.color.colorDiurno);
                break;
            case "vespertino":
                color = context.getResources().getColor(R.color.colorVespertino);
                break;
            case "nocturno":
                color = context.getResources().getColor(R.color.colorNocturno);
                break;
            default:
                color = context.getResources().getColor(android.R.color.white);
                break;
        }
        // Aplicar el color de fondo sin cambiar el drawable
        holder.linearLayoutItem.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return listaTurnos.size();
    }

    public static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtApellido1, txtApellido2, txtPuesto;
        LinearLayout linearLayoutItem;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtApellido1 = itemView.findViewById(R.id.txtApellido1);
            txtApellido2 = itemView.findViewById(R.id.txtApellido2);
            txtPuesto = itemView.findViewById(R.id.txtPuesto);
            linearLayoutItem = itemView.findViewById(R.id.linearLayoutItem);
        }
    }
}
