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

public class AdaptadorAuxiliares extends RecyclerView.Adapter<AdaptadorAuxiliares.ViewHolder> {

    private Context context;
    private List<TrabajadorDTO> listaAuxiliares;
    private OnItemClickListener onItemClickListener;

    public AdaptadorAuxiliares(Context context, List<TrabajadorDTO> listaAuxiliares) {
        this.context = context;
        this.listaAuxiliares = listaAuxiliares;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_auxiliar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrabajadorDTO auxiliar = listaAuxiliares.get(position);
        holder.txtNombre.setText(auxiliar.getNombre() + " " + auxiliar.getApellido1() + " " + auxiliar.getApellido2());
        holder.txtPuesto.setText(auxiliar.getPuesto());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(auxiliar);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAuxiliares.size();
    }

    public interface OnItemClickListener {
        void onItemClick(TrabajadorDTO trabajador);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPuesto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreAuxiliar);
            txtPuesto = itemView.findViewById(R.id.txtPuestoAuxiliar);
        }
    }
}
