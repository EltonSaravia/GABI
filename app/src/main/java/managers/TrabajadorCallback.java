package managers;

import java.util.List;

import dto.TrabajadorDTO;

public interface TrabajadorCallback {
    void onSuccess(List<TrabajadorDTO> trabajadores);
    void onError(String error);


}