package dto;

public class TrabajadorTurnoDTO {
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String puesto;
    private String turno;

    public TrabajadorTurnoDTO(String nombre, String apellido1, String apellido2, String puesto, String turno) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.puesto = puesto;
        this.turno = turno;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getPuesto() {
        return puesto;
    }

    public String getTurno() {
        return turno;
    }
}
