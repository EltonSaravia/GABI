package managers;

import dto.ResidenteDTO;

public interface ResidenteCallback {
    void onSuccess(ResidenteDTO residente);
    void onError(String error);
}
