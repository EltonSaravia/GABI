package com.example.gabi.enfermeria;

public class Administracion {
    private String residente;
    private String medicamento;
    private String trabajador;
    private String fechaHora;
    private String dosis;

    // Constructor
    public Administracion(String residente, String medicamento, String trabajador, String fechaHora, String dosis) {
        this.residente = residente;
        this.medicamento = medicamento;
        this.trabajador = trabajador;
        this.fechaHora = fechaHora;
        this.dosis = dosis;
    }

    // Getters
    public String getResidente() {
        return residente;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public String getTrabajador() {
        return trabajador;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getDosis() {
        return dosis;
    }
}
