package hgcq.callback;

import hgcq.model.dto.EventDTO;

public interface LoginCallback {
    void onSuccess(Boolean isLogin);
    void onError(String message);
}
