package dto;

import java.util.Date;

public class ControlesDTO {
    private int id;
    private int residenteId;
    private Date fecha;
    private String hora;
    private int trabajadorId;
    private String accion;
    private String notas;

    // Constructor
    public ControlesDTO(int id, int residenteId, Date fecha, String hora, int trabajadorId, String accion, String notas) {
        this.id = id;
        this.residenteId = residenteId;
        this.fecha = fecha;
        this.hora = hora;
        this.trabajadorId = trabajadorId;
        this.accion = accion;
        this.notas = notas;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResidenteId() {
        return residenteId;
    }

    public void setResidenteId(int residenteId) {
        this.residenteId = residenteId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getTrabajadorId() {
        return trabajadorId;
    }

    public void setTrabajadorId(int trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
