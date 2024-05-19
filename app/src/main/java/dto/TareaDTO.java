package dto;

import java.io.Serializable;

public class TareaDTO implements Serializable {
    private int id;
    private String titulo;
    private String notas;
    private String fechaTareaAsignada;
    private String horaTareaAsignada;
    private int trabajadorId;

    public TareaDTO(int id, String titulo, String notas, String fechaTareaAsignada, String horaTareaAsignada, int trabajadorId) {
        this.id = id;
        this.titulo = titulo;
        this.notas = notas;
        this.fechaTareaAsignada = fechaTareaAsignada;
        this.horaTareaAsignada = horaTareaAsignada;
        this.trabajadorId = trabajadorId;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNotas() {
        return notas;
    }

    public String getFechaTareaAsignada() {
        return fechaTareaAsignada;
    }

    public String getHoraTareaAsignada() {
        return horaTareaAsignada;
    }

    public int getTrabajadorId() {
        return trabajadorId;
    }
}
