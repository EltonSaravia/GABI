package managers;

import java.util.List;
import dto.TurnoDTO;

public interface TurnoCallback {
    void onSuccess(List<TurnoDTO> turnos);
    void onSuccess(String message);
    void onError(String error);
}
