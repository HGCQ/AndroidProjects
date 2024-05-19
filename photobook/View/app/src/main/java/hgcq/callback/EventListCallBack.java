package hgcq.callback;

import java.util.List;

import hgcq.model.dto.EventDTO;

public interface EventListCallBack {
    void onSuccess(List<EventDTO> eventDTOs);
    void onError(String message);
}
