import java.util.List;

import dto.EventoDTO;

public interface EventosCallback {
    void onEventosRecibidos(List<EventoDTO> eventos);
    void onError(String error);
}
