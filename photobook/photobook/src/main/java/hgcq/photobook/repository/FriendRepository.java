package hgcq.photobook.repository;

import hgcq.photobook.domain.Friend;
import hgcq.photobook.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 친구 추가
 * 친구 삭제
 * 친구 관계 조회
 * 친구 리스트 조회
 */

@Repository
@RequiredArgsConstructor
public class FriendRepository {

    @PersistenceContext
    private final EntityManager em;

    /**
     * 친구 추가
     *
     * @param friend 친구
     */
    public void save(Friend friend) {
        em.persist(friend);
    }

    /**
     * 친구 삭제
     *
     * @param friend 친구
     */
    public void delete(Friend friend) {
        em.remove(friend);
    }

    /**
     * 친구 관계 조회
     *
     * @param member 회원
     * @param friend 친구
     * @return 친구 관계
     */
    public List<Friend> findFriend(Member member, Member friend) {
        return em.createQuery("select f from Friend f where f.member = :member and f.friendMember = :friend", Friend.class)
                .setParameter("member", member)
                .setParameter("friend", friend)
                .getResultList();
    }

    /**
     * 친구 검색
     *
     * @param name 이름
     * @return 친구
     */
    public List<Member> findFriendByName(String name, Member member) {
        return em.createQuery("select f.friendMember from Friend f where f.friendMember.name LIKE :name and f.member = :member order by f.friendMember.name", Member.class)
                .setParameter("name", "%" + name + "%")
                .setParameter("member", member)
                .getResultList();
    }

    /**
     * 친구 리스트 조회
     *
     * @param member 회원
     * @return 친구 리스트
     */
    public List<Member> friends(Member member) {
        return em.createQuery("select f.friendMember from Friend f where member = :member order by f.friendMember.name", Member.class)
                .setParameter("member", member)
                .getResultList();
    }
}
