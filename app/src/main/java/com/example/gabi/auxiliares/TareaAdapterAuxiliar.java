package com.example.gabi.auxiliares;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabi.R;

import java.util.List;

import dto.TareaDTO;
import managers.TareaCallback;
import managers.TareaManager;

public class TareaAdapterAuxiliar extends RecyclerView.Adapter<TareaAdapterAuxiliar.TareaViewHolder> {

    private List<TareaDTO> tareaList;
    private Context context;
    private TareaManager tareaManager;

    public TareaAdapterAuxiliar(List<TareaDTO> tareaList, Context context, TareaManager tareaManager) {
        this.tareaList = tareaList;
        this.context = context;
        this.tareaManager = tareaManager;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea_auxiliar, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        TareaDTO tarea = tareaList.get(position);
        holder.tvTituloTarea.setText(tarea.getTitulo());
        holder.tvEstadoTarea.setText(tarea.getEstado() == 0 ? "Pendiente" : "Completada");

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(tarea.getEstado() == 1);
        holder.checkBox.setEnabled(tarea.getEstado() == 0);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showConfirmationDialog(tarea, holder);
            }
        });
    }

    private void showConfirmationDialog(TareaDTO tarea, TareaViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Completar Tarea");

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_completar_tarea, (ViewGroup) null, false);
        final EditText input = viewInflated.findViewById(R.id.etNotas);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String notas = input.getText().toString();
                tareaManager.completarTarea(tarea.getId(), notas, new TareaCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(context, "Tarea completada", Toast.LENGTH_SHORT).show();
                        tarea.setEstado(1);
                        holder.tvEstadoTarea.setText("Completada");
                        holder.checkBox.setEnabled(false);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                        holder.checkBox.setChecked(false);
                    }

                    @Override
                    public void onSuccess(List<TareaDTO> tareaList) {
                        // No se usa aquí
                    }
                });
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                holder.checkBox.setChecked(false);
            }
        });

        builder.show();
    }

    @Override
    public int getItemCount() {
        return tareaList.size();
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTituloTarea, tvEstadoTarea;
        CheckBox checkBox;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTituloTarea = itemView.findViewById(R.id.tvTituloTarea);
            tvEstadoTarea = itemView.findViewById(R.id.tvEstadoTarea);
            checkBox = itemView.findViewById(R.id.checkBoxComplete);
        }
    }
}
