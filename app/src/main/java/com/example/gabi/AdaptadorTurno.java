package com.example.gabi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabi.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import dto.TurnoDTO;

public class AdaptadorTurno extends RecyclerView.Adapter<AdaptadorTurno.ViewHolder> {

    private List<TurnoDTO> listaTurnos;
    private LayoutInflater inflater;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    // Constructor
    public AdaptadorTurno(Context context, List<TurnoDTO> listaTurnos) {
        this.inflater = LayoutInflater.from(context);
        this.listaTurnos = listaTurnos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_turno, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TurnoDTO turno = listaTurnos.get(position);
        holder.bind(turno);
    }

    @Override
    public int getItemCount() {
        return listaTurnos.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtApellido1, txtApellido2, txtPuesto, txtTipoTurno;

        ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtApellido1 = itemView.findViewById(R.id.txtApellido1);
            txtApellido2 = itemView.findViewById(R.id.txtApellido2);
            txtPuesto = itemView.findViewById(R.id.txtPuesto);
            txtTipoTurno = itemView.findViewById(R.id.txtTipoTurno);
        }

        void bind(TurnoDTO turno) {
            txtNombre.setText(turno.getNombre());
            txtApellido1.setText(turno.getApellido1());
            txtApellido2.setText(turno.getApellido2());
            txtPuesto.setText(turno.getPuesto());
            txtTipoTurno.setText(turno.getTipo());
        }
    }
}
