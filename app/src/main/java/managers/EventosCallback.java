package managers;

import java.util.List;

import dto.EventoDTO;

public interface EventosCallback {
    void onSuccess(List<EventoDTO> eventos);
    void onError(String error);
}
