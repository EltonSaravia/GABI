package dto;

import java.util.Date;

public class MedicamentoDTO {
    private int id;
    private int dniResidente;
    private String nombreMedicamento;
    private String dosis;
    private Date fechaInicio;
    private Date fechaFin;
    private String observaciones;

    // Constructor
    public MedicamentoDTO(int id, int dniResidente, String nombreMedicamento, String dosis, Date fechaInicio, Date fechaFin, String observaciones) {
        this.id = id;
        this.dniResidente = dniResidente;
        this.nombreMedicamento = nombreMedicamento;
        this.dosis = dosis;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.observaciones = observaciones;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDniResidente() {
        return dniResidente;
    }

    public void setDniResidente(int dniResidente) {
        this.dniResidente = dniResidente;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
