package com.example.gabi;

public class Mensaje {
    private String mensaje;
    private String nombre;
    private String fotoPerfil;
    private String typeMensaje;

    public Mensaje() {
    }

    public Mensaje(String mensaje, String nombre, String fotoPerfil, String typeMensaje) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.typeMensaje = typeMensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getTypeMensaje() {
        return typeMensaje;
    }

    public void setTypeMensaje(String typeMensaje) {
        this.typeMensaje = typeMensaje;
    }
}
