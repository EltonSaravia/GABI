package dto;

import java.util.Date;

public class ResidenteDTO {
    private int id;
    private String dni;
    private String nombre;
    private String apellidos;
    private Date fechaNacimiento;
    private String ar;
    private String nss;
    private String numeroCuentaBancaria;
    private String observaciones;
    private int medicamentos;
    private Date fechaIngreso;
    private String activo;
    private String empadronamiento;
    private int edad;
    private int mesCumple;
    private byte[] foto;
    private int habitacionId;
    private boolean estado;

    // Constructor
    public ResidenteDTO(int id, String dni, String nombre, String apellidos, Date fechaNacimiento, String ar, String nss,
                        String numeroCuentaBancaria, String observaciones, int medicamentos, Date fechaIngreso,
                        String activo, String empadronamiento, int edad, int mesCumple, byte[] foto, int habitacionId, boolean estado) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.ar = ar;
        this.nss = nss;
        this.numeroCuentaBancaria = numeroCuentaBancaria;
        this.observaciones = observaciones;
        this.medicamentos = medicamentos;
        this.fechaIngreso = fechaIngreso;
        this.activo = activo;
        this.empadronamiento = empadronamiento;
        this.edad = edad;
        this.mesCumple = mesCumple;
        this.foto = foto;
        this.habitacionId = habitacionId;
        this.estado = estado;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getAr() { return ar; }
    public void setAr(String ar) { this.ar = ar; }

    public String getNss() { return nss; }
    public void setNss(String nss) { this.nss = nss; }

    public String getNumeroCuentaBancaria() { return numeroCuentaBancaria; }
    public void setNumeroCuentaBancaria(String numeroCuentaBancaria) { this.numeroCuentaBancaria = numeroCuentaBancaria; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public int getMedicamentos() { return medicamentos; }
    public void setMedicamentos(int medicamentos) { this.medicamentos = medicamentos; }

    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getActivo() { return activo; }
    public void setActivo(String activo) { this.activo = activo; }

    public String getEmpadronamiento() { return empadronamiento; }
    public void setEmpadronamiento(String empadronamiento) { this.empadronamiento = empadronamiento; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public int getMesCumple() { return mesCumple; }
    public void setMesCumple(int mesCumple) { this.mesCumple = mesCumple; }

    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }

    public int getHabitacionId() { return habitacionId; }
    public void setHabitacionId(int habitacionId) { this.habitacionId = habitacionId; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}
