package dto;

public class PagoDTO {
    private int id;
    private int residenteId;
    private String fechaEntrada;
    private String fechaFin;
    private String fechaCorrespondePago;
    private boolean estadoImpago;

    public PagoDTO(int id, int residenteId, String fechaEntrada, String fechaFin, String fechaCorrespondePago, boolean estadoImpago) {
        this.id = id;
        this.residenteId = residenteId;
        this.fechaEntrada = fechaEntrada;
        this.fechaFin = fechaFin;
        this.fechaCorrespondePago = fechaCorrespondePago;
        this.estadoImpago = estadoImpago;
    }

    public int getId() {
        return id;
    }

    public int getResidenteId() {
        return residenteId;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public String getFechaCorrespondePago() {
        return fechaCorrespondePago;
    }

    public boolean isEstadoImpago() {
        return estadoImpago;
    }
}
