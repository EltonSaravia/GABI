package com.example.gabi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.txtTipoTurno.setText(turno.getTipo());
    }

    @Override
    public int getItemCount() {
        return listaTurnos.size();
    }

    public static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtApellido1, txtApellido2, txtPuesto, txtTipoTurno;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtApellido1 = itemView.findViewById(R.id.txtApellido1);
            txtApellido2 = itemView.findViewById(R.id.txtApellido2);
            txtPuesto = itemView.findViewById(R.id.txtPuesto);
            txtTipoTurno = itemView.findViewById(R.id.txtTipoTurno);
        }
    }
}
