package hgcq.photobook.dto;

import java.io.Serializable;
import java.util.Objects;

public class EventMemberId implements Serializable {
    private Long eventId;
    private Long memberId;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

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
