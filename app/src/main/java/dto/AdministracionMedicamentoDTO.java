package dto;

import java.util.Date;

public class AdministracionMedicamentoDTO {
    private int id;
    private int medicamentoId;
    private int residenteId;
    private int trabajadorId;
    private Date fechaAdministracion;
    private String horaAdministracion;
    private String dosisAdministrada;
    private String observaciones;

    // Constructor
    public AdministracionMedicamentoDTO(int id, int medicamentoId, int residenteId, int trabajadorId,
                                        Date fechaAdministracion, String horaAdministracion,
                                        String dosisAdministrada, String observaciones) {
        this.id = id;
        this.medicamentoId = medicamentoId;
        this.residenteId = residenteId;
        this.trabajadorId = trabajadorId;
        this.fechaAdministracion = fechaAdministracion;
        this.horaAdministracion = horaAdministracion;
        this.dosisAdministrada = dosisAdministrada;
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

    public int getResidenteId() {
        return residenteId;
    }

    public void setResidenteId(int residenteId) {
        this.residenteId = residenteId;
    }

    public int getTrabajadorId() {
        return trabajadorId;
    }

    public void setTrabajadorId(int trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public Date getFechaAdministracion() {
        return fechaAdministracion;
    }

    public void setFechaAdministracion(Date fechaAdministracion) {
        this.fechaAdministracion = fechaAdministracion;
    }

    public String getHoraAdministracion() {
        return horaAdministracion;
    }

    public void setHoraAdministracion(String horaAdministracion) {
        this.horaAdministracion = horaAdministracion;
    }

    public String getDosisAdministrada() {
        return dosisAdministrada;
    }

    public void setDosisAdministrada(String dosisAdministrada) {
        this.dosisAdministrada = dosisAdministrada;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
