package dto;

import java.sql.Time;
import java.util.Date;

public class EventoDTO {
    private int id;
    private int residenteId;
    private Date fechaCita;
    private Time horaCita;
    private String lugarCita;
    private String motivoCita;
    private String detalles;
    private String nombreResidente;
    private String apellidosResidente;

    public EventoDTO(int id, int residenteId, Date fechaCita, Time horaCita, String lugarCita, String motivoCita, String detalles, String nombreResidente, String apellidosResidente) {
        this.id = id;
        this.residenteId = residenteId;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.lugarCita = lugarCita;
        this.motivoCita = motivoCita;
        this.detalles = detalles;
        this.nombreResidente = nombreResidente;
        this.apellidosResidente = apellidosResidente;
    }

    // Getters y setters
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

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Time getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(Time horaCita) {
        this.horaCita = horaCita;
    }

    public String getLugarCita() {
        return lugarCita;
    }

    public void setLugarCita(String lugarCita) {
        this.lugarCita = lugarCita;
    }

    public String getMotivoCita() {
        return motivoCita;
    }

    public void setMotivoCita(String motivoCita) {
        this.motivoCita = motivoCita;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getNombreResidente() {
        return nombreResidente;
    }

    public void setNombreResidente(String nombreResidente) {
        this.nombreResidente = nombreResidente;
    }

    public String getApellidosResidente() {
        return apellidosResidente;
    }

    public void setApellidosResidente(String apellidosResidente) {
        this.apellidosResidente = apellidosResidente;
    }
}
