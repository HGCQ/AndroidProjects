package hgcq.photobook.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
public class FriendId implements Serializable {

    private Long memberId;
    private Long friendId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendId friendId1 = (FriendId) o;
        return Objects.equals(getMemberId(), friendId1.getMemberId()) && Objects.equals(getFriendId(), friendId1.getFriendId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMemberId(), getFriendId());
    }
}
