package com.example.gabi;

import com.example.gabi.Mensaje;

import java.util.Map;

public class MensajeEnviar extends Mensaje {
    private Map<String, String> timestamp;

    public MensajeEnviar(String mensaje, String nombre, String fotoPerfil, String type_mensaje, Map<String, String> timestamp) {
        super(mensaje, nombre, fotoPerfil, type_mensaje);
        this.timestamp = timestamp;
    }

    public MensajeEnviar() {
    }

    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }
}
