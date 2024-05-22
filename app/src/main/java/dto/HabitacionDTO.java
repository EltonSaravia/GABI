package dto;

public class HabitacionDTO {

    private int id;
    private int numero;
    private String piso;
    private String observaciones;
    private boolean ocupada;

    public HabitacionDTO(int id, int numero, String piso, String observaciones, boolean ocupada) {
        this.id = id;
        this.numero = numero;
        this.piso = piso;
        this.observaciones = observaciones;
        this.ocupada = ocupada;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public String getPiso() {
        return piso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public boolean isOcupada() {
        return ocupada;
    }
}
