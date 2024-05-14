package managers;

import java.util.List;

import dto.TurnoDTO;

public interface TurnoCallback {
    void onSuccess(List<TurnoDTO> turnos);
    void onError(String error);
}