package com.example.gabi.auxiliares;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gabi.R;
import dto.ResidenteDTO;
import java.util.List;

public class ResidentesInfoCompletaAdapter extends RecyclerView.Adapter<ResidentesInfoCompletaAdapter.ViewHolder> {

    private List<ResidenteDTO> listaResidentes;

    public ResidentesInfoCompletaAdapter(List<ResidenteDTO> listaResidentes) {
        this.listaResidentes = listaResidentes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_residente_auxiliar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResidenteDTO residente = listaResidentes.get(position);
        holder.nombre.setText(residente.getNombre());
        holder.apellidos.setText(residente.getApellidos());
        holder.fechaNacimiento.setText(residente.getFechaNacimiento() != null ? residente.getFechaNacimiento().toString() : "");
        holder.observaciones.setText(residente.getObservaciones());
        holder.telefono.setText(residente.getTelefono());
        holder.habitacionId.setText(residente.getHabitacionId() != null ? residente.getHabitacionId().toString() : "N/A");

        if (residente.getFoto() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(residente.getFoto(), 0, residente.getFoto().length);
            holder.foto.setImageBitmap(bitmap);
        } else {
            holder.foto.setImageResource(R.drawable.foto_generica);
        }

        holder.itemView.setOnLongClickListener(v -> {
            // Mantén presionado por 10 segundos
            holder.itemView.postDelayed(() -> mostrarDialogoConfirmacion(v.getContext(), residente), 10000);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaResidentes.size();
    }

    private void mostrarDialogoConfirmacion(Context context, ResidenteDTO residente) {
        new AlertDialog.Builder(context)
                .setTitle("Enviar SMS de emergencia")
                .setMessage("¿Deseas enviar un SMS de emergencia a los familiares de " + residente.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    enviarSMS(context, residente.getTlfnFamiliar1(), "Mensaje de emergencia para " + residente.getNombre());
                    if (residente.getTlfnFamiliar2() != null) {
                        enviarSMS(context, residente.getTlfnFamiliar2(), "Mensaje de emergencia para " + residente.getNombre());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void enviarSMS(Context context, String numero, String mensaje) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numero, null, mensaje, null, null);
            Toast.makeText(context, "SMS enviado a " + numero, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error al enviar SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, apellidos, fechaNacimiento, observaciones, telefono, habitacionId;
        ImageView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            apellidos = itemView.findViewById(R.id.apellidos);
            fechaNacimiento = itemView.findViewById(R.id.fecha_nacimiento);
            observaciones = itemView.findViewById(R.id.observaciones);
            telefono = itemView.findViewById(R.id.telefono);
            habitacionId = itemView.findViewById(R.id.habitacion_id);
            foto = itemView.findViewById(R.id.foto);
        }
    }
}
