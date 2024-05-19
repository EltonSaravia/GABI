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
import dto.TareaDTO;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.ViewHolder> {

    private Context context;
    private List<TareaDTO> tareaList;

    public TareaAdapter(Context context, List<TareaDTO> tareaList) {
        this.context = context;
        this.tareaList = tareaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tarea_pantalla_administrador, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TareaDTO tarea = tareaList.get(position);

        holder.textViewTitulo.setText(tarea.getTitulo());
        holder.textViewNotas.setText(tarea.getNotas());
        holder.textViewFechaHora.setText(tarea.getFechaTareaAsignada() + " " + tarea.getHoraTareaAsignada());
    }

    @Override
    public int getItemCount() {
        return tareaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo;
        TextView textViewNotas;
        TextView textViewFechaHora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewNotas = itemView.findViewById(R.id.textViewNotas);
            textViewFechaHora = itemView.findViewById(R.id.textViewFechaHora);
        }
    }
}
