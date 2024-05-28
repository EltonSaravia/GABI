package com.example.gabi;

import com.google.firebase.database.ServerValue;

public class MensajeEnviar extends Mensaje {
    private Object hora;

    public MensajeEnviar() {
    }

    public MensajeEnviar(String mensaje, String nombre) {
        super(mensaje, nombre, null, "text");
        this.hora = ServerValue.TIMESTAMP;
    }

    public Object getHora() {
        return hora;
    }

    public void setHora(Object hora) {
        this.hora = hora;
    }
}
