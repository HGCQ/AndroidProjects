package hgcq.callback;

import java.util.List;

import hgcq.model.dto.MemberDTO;

public interface MemberCallback {
    void onSuccess(List<MemberDTO> friends);
    void onError(String message);
}
