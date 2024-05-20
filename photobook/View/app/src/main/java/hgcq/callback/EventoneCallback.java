package hgcq.callback;

import hgcq.model.dto.EventDTO;

public interface EventoneCallback {
    void onSuccess(EventDTO eventDTO);
    void onError(String message);
}
