package hgcq.callback;

import hgcq.model.dto.EventDTO;

public interface EventOneCallback {
    void onSuccess(EventDTO eventDTO);
    void onError(String message);
}
