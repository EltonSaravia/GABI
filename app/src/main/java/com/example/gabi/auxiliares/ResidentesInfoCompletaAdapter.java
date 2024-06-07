package com.example.gabi.auxiliares;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
        holder.dni.setText(residente.getDni());
        holder.telefono.setText(residente.getTelefono());
        holder.email.setText(residente.getEmail());
        holder.fechaNacimiento.setText(residente.getFechaNacimiento() != null ? residente.getFechaNacimiento().toString() : "");
        holder.fechaIngreso.setText(residente.getFechaIngreso() != null ? residente.getFechaIngreso().toString() : "");
        holder.ar.setText(residente.getAr());
        holder.nss.setText(residente.getNss());
        holder.numeroCuentaBancaria.setText(residente.getNumeroCuentaBancaria());
        holder.observaciones.setText(residente.getObservaciones());
        holder.medicamentos.setText(String.valueOf(residente.getMedicamentos()));
        holder.activo.setText(residente.getActivo());
        holder.empadronamiento.setText(residente.getEmpadronamiento());
        holder.edad.setText(String.valueOf(residente.getEdad()));
        holder.mesCumple.setText(String.valueOf(residente.getMesCumple()));
        holder.habitacionId.setText(residente.getHabitacionId() != null ? residente.getHabitacionId().toString() : "N/A");
        holder.estado.setText(residente.isEstado() ? "Activo" : "Inactivo");

        if (residente.getFoto() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(residente.getFoto(), 0, residente.getFoto().length);
            holder.foto.setImageBitmap(bitmap);
        } else {
            holder.foto.setImageResource(R.drawable.foto_generica);
        }
    }

    @Override
    public int getItemCount() {
        return listaResidentes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, apellidos, dni, telefono, email, fechaNacimiento, fechaIngreso, ar, nss, numeroCuentaBancaria,
                observaciones, medicamentos, activo, empadronamiento, edad, mesCumple, habitacionId, estado;
        ImageView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            apellidos = itemView.findViewById(R.id.apellidos);
            dni = itemView.findViewById(R.id.dni);
            telefono = itemView.findViewById(R.id.telefono);
            email = itemView.findViewById(R.id.email);
            fechaNacimiento = itemView.findViewById(R.id.fecha_nacimiento);
            fechaIngreso = itemView.findViewById(R.id.fecha_ingreso);
            ar = itemView.findViewById(R.id.ar);
            nss = itemView.findViewById(R.id.nss);
            numeroCuentaBancaria = itemView.findViewById(R.id.numero_cuenta_bancaria);
            observaciones = itemView.findViewById(R.id.observaciones);
            medicamentos = itemView.findViewById(R.id.medicamentos);
            activo = itemView.findViewById(R.id.activo);
            empadronamiento = itemView.findViewById(R.id.empadronamiento);
            edad = itemView.findViewById(R.id.edad);
            mesCumple = itemView.findViewById(R.id.mes_cumple);
            habitacionId = itemView.findViewById(R.id.habitacion_id);
            estado = itemView.findViewById(R.id.estado);
            foto = itemView.findViewById(R.id.foto);
        }
    }
}
