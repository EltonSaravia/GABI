package dto;

import java.util.Date;

public class RegistroEntradasSalidasDTO {
    private int id;
    private int trabajadorId;
    private Date fecha;
    private Date horaEntrada;
    private Date horaSalida;
    private String notas;

    // Constructor
    public RegistroEntradasSalidasDTO(int id, int trabajadorId, Date fecha, Date horaEntrada, Date horaSalida, String notas) {
        this.id = id;
        this.trabajadorId = trabajadorId;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.notas = notas;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrabajadorId() {
        return trabajadorId;
    }

    public void setTrabajadorId(int trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
