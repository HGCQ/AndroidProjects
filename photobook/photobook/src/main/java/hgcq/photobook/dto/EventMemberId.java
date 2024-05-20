package hgcq.photobook.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
public class EventMemberId implements Serializable {
    private Long eventId;
    private Long memberId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventMemberId that = (EventMemberId) o;
        return Objects.equals(getEventId(), that.getEventId()) && Objects.equals(getMemberId(), that.getMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventId(), getMemberId());
    }
}
