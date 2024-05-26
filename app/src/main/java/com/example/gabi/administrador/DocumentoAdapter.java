package com.example.gabi.administrador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabi.R;

import java.util.List;

import dto.DocumentoDTO;

public class DocumentoAdapter extends RecyclerView.Adapter<DocumentoAdapter.DocumentoViewHolder> {

    private List<DocumentoDTO> documentoList;
    private OnDocumentoEliminarListener eliminarListener;
    private OnDocumentoDescargarListener descargarListener;

    public DocumentoAdapter(List<DocumentoDTO> documentoList, OnDocumentoEliminarListener eliminarListener, OnDocumentoDescargarListener descargarListener) {
        this.documentoList = documentoList;
        this.eliminarListener = eliminarListener;
        this.descargarListener = descargarListener;
    }

    @NonNull
    @Override
    public DocumentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_documento, parent, false);
        return new DocumentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentoViewHolder holder, int position) {
        DocumentoDTO documento = documentoList.get(position);
        holder.tvTitulo.setText(documento.getTitulo());
        holder.tvDescripcion.setText(documento.getDescripcion());
        holder.tvFechaSubida.setText(documento.getFechaSubida());
        holder.btnEliminar.setOnClickListener(v -> eliminarListener.onDocumentoEliminar(documento.getId()));
        holder.btnDescargar.setOnClickListener(v -> descargarListener.onDocumentoDescargar(documento.getId()));
    }

    @Override
    public int getItemCount() {
        return documentoList.size();
    }

    public static class DocumentoViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo, tvDescripcion, tvFechaSubida;
        ImageButton btnEliminar, btnDescargar;

        public DocumentoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFechaSubida = itemView.findViewById(R.id.tvFechaSubida);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnDescargar = itemView.findViewById(R.id.btnDescargar);
        }
    }

    public interface OnDocumentoEliminarListener {
        void onDocumentoEliminar(int id);
    }

    public interface OnDocumentoDescargarListener {
        void onDocumentoDescargar(int id);
    }
}
