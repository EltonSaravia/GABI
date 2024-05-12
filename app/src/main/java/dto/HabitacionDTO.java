package dto;

public class HabitacionDTO {
    private int id;
    private String numero;
    private String piso;
    private int capacidad;
    private String observaciones;

    // Constructor
    public HabitacionDTO(int id, String numero, String piso, int capacidad, String observaciones) {
        this.id = id;
        this.numero = numero;
        this.piso = piso;
        this.capacidad = capacidad;
        this.observaciones = observaciones;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
