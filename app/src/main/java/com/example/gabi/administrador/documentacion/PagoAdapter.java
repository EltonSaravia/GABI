package com.example.gabi.administrador.documentacion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabi.R;

import java.util.List;

import dto.PagoDTO;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.PagoViewHolder> {

    private List<PagoDTO> pagoList;

    public PagoAdapter(List<PagoDTO> pagoList) {
        this.pagoList = pagoList;
    }

    @NonNull
    @Override
    public PagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pago, parent, false);
        return new PagoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoViewHolder holder, int position) {
        PagoDTO pago = pagoList.get(position);
        holder.tvResidenteId.setText(String.valueOf(pago.getResidenteId()));
        holder.tvFechaEntrada.setText(pago.getFechaEntrada());
        holder.tvFechaCorrespondePago.setText(pago.getFechaCorrespondePago());
        holder.tvEstadoImpago.setText(pago.isEstadoImpago() ? "Impago" : "Pagado");

        // Si alguna información es null, simplemente no la mostramos
        if (pago.getFechaFin() == null) {
            holder.tvFechaEntrada.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return pagoList.size();
    }

    static class PagoViewHolder extends RecyclerView.ViewHolder {

        TextView tvResidenteId, tvFechaEntrada, tvFechaCorrespondePago, tvEstadoImpago;

        public PagoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvResidenteId = itemView.findViewById(R.id.tvResidenteId);
            tvFechaEntrada = itemView.findViewById(R.id.tvFechaEntrada);
            tvFechaCorrespondePago = itemView.findViewById(R.id.tvFechaCorrespondePago);
            tvEstadoImpago = itemView.findViewById(R.id.tvEstadoImpago);
        }
    }
}
