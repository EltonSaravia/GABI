package managers;

import java.util.List;
import dto.TareaDTO;

public interface TareaCallback {
    void onSuccess(String message);
    void onError(String error);
    void onSuccess(List<TareaDTO> tareaList);
}
