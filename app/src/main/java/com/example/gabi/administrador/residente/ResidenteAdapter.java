package com.example.gabi.administrador.residente;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gabi.R;
import dto.ResidenteDTO;
import java.util.List;

public class ResidenteAdapter extends RecyclerView.Adapter<ResidenteAdapter.ResidenteViewHolder> {

    private List<ResidenteDTO> residentes;

    public ResidenteAdapter(List<ResidenteDTO> residentes) {
        this.residentes = residentes;
    }

    @NonNull
    @Override
    public ResidenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_residente, parent, false);
        return new ResidenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResidenteViewHolder holder, int position) {
        ResidenteDTO residente = residentes.get(position);

        // Cambiar el fondo según el estado activo del residente
        if ("false".equals(residente.getActivo())) {
            holder.itemView.setBackgroundColor(Color.RED);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        holder.nombre.setText(residente.getNombre() != null ? residente.getNombre() : "");
        holder.dni.setText(residente.getDni() != null ? residente.getDni() : "");
        holder.telefono.setText(residente.getTelefono() != null ? residente.getTelefono() : "");
        holder.email.setText(residente.getEmail() != null ? residente.getEmail() : "");
        holder.apellidos.setText(residente.getApellidos() != null ? residente.getApellidos() : "");
        holder.fechaNacimiento.setText(residente.getFechaNacimiento() != null ? residente.getFechaNacimiento().toString() : "");
        holder.ar.setText(residente.getAr() != null ? residente.getAr() : "");
        holder.nss.setText(residente.getNss() != null ? residente.getNss() : "");
        holder.numeroCuentaBancaria.setText(residente.getNumeroCuentaBancaria() != null ? residente.getNumeroCuentaBancaria() : "");
        holder.observaciones.setText(residente.getObservaciones() != null ? residente.getObservaciones() : "");
        holder.medicamentos.setText(String.valueOf(residente.getMedicamentos()));
        holder.fechaIngreso.setText(residente.getFechaIngreso() != null ? residente.getFechaIngreso().toString() : "");
        holder.activo.setText(residente.getActivo() != null ? residente.getActivo() : "");
        holder.empadronamiento.setText(residente.getEmpadronamiento() != null ? residente.getEmpadronamiento() : "");
        holder.edad.setText(String.valueOf(residente.getEdad()));
        holder.mesCumple.setText(String.valueOf(residente.getMesCumple()));
        holder.habitacionId.setText(String.valueOf(residente.getHabitacionId()));
        holder.estado.setText(String.valueOf(residente.isEstado()));
    }

    @Override
    public int getItemCount() {
        return residentes.size();
    }

    static class ResidenteViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, dni, telefono, email, apellidos, fechaNacimiento, ar, nss, numeroCuentaBancaria, observaciones, medicamentos, fechaIngreso, activo, empadronamiento, edad, mesCumple, habitacionId, estado;

        ResidenteViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            dni = itemView.findViewById(R.id.dni);
            telefono = itemView.findViewById(R.id.telefono);
            email = itemView.findViewById(R.id.email);
            apellidos = itemView.findViewById(R.id.apellidos);
            fechaNacimiento = itemView.findViewById(R.id.fecha_nacimiento);
            ar = itemView.findViewById(R.id.ar);
            nss = itemView.findViewById(R.id.nss);
            numeroCuentaBancaria = itemView.findViewById(R.id.numero_cuenta_bancaria);
            observaciones = itemView.findViewById(R.id.observaciones);
            medicamentos = itemView.findViewById(R.id.medicamentos);
            fechaIngreso = itemView.findViewById(R.id.fecha_ingreso);
            activo = itemView.findViewById(R.id.activo);
            empadronamiento = itemView.findViewById(R.id.empadronamiento);
            edad = itemView.findViewById(R.id.edad);
            mesCumple = itemView.findViewById(R.id.mes_cumple);
            habitacionId = itemView.findViewById(R.id.habitacion_id);
            estado = itemView.findViewById(R.id.estado);
        }
    }
}
