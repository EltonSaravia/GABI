package dto;

import java.util.Date;

public class StockMedicamentoDTO {
    private int id;
    private int medicamentoId;
    private int cantidad;
    private String unidad;
    private Date fechaActualizacion;
    private String observaciones;

    // Constructor
    public StockMedicamentoDTO(int id, int medicamentoId, int cantidad, String unidad, Date fechaActualizacion, String observaciones) {
        this.id = id;
        this.medicamentoId = medicamentoId;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.fechaActualizacion = fechaActualizacion;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(int medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
