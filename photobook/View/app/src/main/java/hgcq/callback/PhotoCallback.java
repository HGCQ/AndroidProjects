package hgcq.callback;

import java.util.List;

public interface PhotoCallback {
    void onSuccess(List<String> photoPaths);
    void onError(String message);
}
