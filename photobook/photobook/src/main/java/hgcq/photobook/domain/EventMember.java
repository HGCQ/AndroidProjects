package hgcq.photobook.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventMember {

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;
}
