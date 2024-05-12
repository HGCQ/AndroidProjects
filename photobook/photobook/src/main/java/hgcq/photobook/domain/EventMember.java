package hgcq.photobook.domain;

import hgcq.photobook.dto.EventMemberId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(EventMemberId.class)
public class EventMember {

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    public EventMember(Long eventId, Long memberId) {
        this.eventId = eventId;
        this.memberId = memberId;
    }
}
