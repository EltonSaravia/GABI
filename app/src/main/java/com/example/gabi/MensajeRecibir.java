package com.example.gabi;

public class MensajeRecibir extends Mensaje {
    private Long timestamp;

    public MensajeRecibir() {
    }

    public MensajeRecibir(String mensaje, String nombre, String fotoPerfil, String type_mensaje, Long timestamp) {
        super(mensaje, nombre, fotoPerfil, type_mensaje);
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHora() {
        // Convertir el timestamp a una hora legible
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        java.util.Date date = new java.util.Date(timestamp);
        return sdf.format(date);
    }
}
