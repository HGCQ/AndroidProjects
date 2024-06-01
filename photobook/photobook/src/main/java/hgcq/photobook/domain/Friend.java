package hgcq.photobook.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(FriendId.class)
public class Friend {

    @Id
    @Column(name="member_id")
    private Long memberId;

    @Id
    @Column(name="friend_id")
    private Long friendId;

    public Friend(Member member, Member friendMember) {
        this.member = member;
        this.friendMember = friendMember;
        this.memberId = member.getId();
        this.friendId = friendMember.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="friend_id", insertable = false, updatable = false)
    private Member friendMember;
}
