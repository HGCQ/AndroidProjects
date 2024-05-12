package hgcq.photobook.repository;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.EventMember;
import hgcq.photobook.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventMemberRepository {

    @PersistenceContext
    private final EntityManager em;

    /**
     * 이벤트와 회원 연결
     *
     * @param eventMember 이벤트와 회원 연결
     */
    public void save(EventMember eventMember) {
        em.persist(eventMember);
    }

    /**
     * 이벤트에 해당하는 이벤트멤버 가져오기
     *
     * @param event 이벤트
     * @return 이벤트멤버 리스트
     */
    public List<EventMember> findByEvent(Event event) {
        return em.createQuery("select em from EventMember em where event = :event", EventMember.class)
                .setParameter("event", event)
                .getResultList();
    }

    /**
     * 이벤트멤버 하나 조회
     *
     * @param event  이벤트
     * @param member 멤버
     * @return 이벤트멤버
     */
    public EventMember findOne(Event event, Member member) {
        return em.createQuery("select em from EventMember em where em.event = :event and em.member = :member"
                        , EventMember.class)
                .setParameter("event", event)
                .setParameter("member", member)
                .getSingleResult();
    }

    /**
     * 이벤트멤버 전체 삭제
     *
     * @param eventMembers 이벤트멤버
     */
    public void deleteAll(List<EventMember> eventMembers) {
        for (EventMember eventMember : eventMembers) {
            em.remove(eventMember);
        }
    }

    /**
     * 회원이 가진 이벤트 리스트 조회
     *
     * @param member 회원
     * @return 이벤트 리스트
     */
    public List<Event> findEventList(Member member) {
        return em.createQuery("select em.event from EventMember em where em.member = :member", Event.class)
                .setParameter("member", member)
                .getResultList();
    }

    /**
     * 이벤트 회원 조회
     *
     * @param event 이벤트
     * @return 회원 리스트
     */
    public List<Member> findMemberByEvent(Event event) {
        return em.createQuery("select em.member from EventMember em where em.event = :event", Member.class)
                .setParameter("event", event)
                .getResultList();
    }

    /**
     * 회원 전체 리스트
     *
     * @return 회원 id 리스트
     */
    public List<Member> findMemberByEvent() {
        return em.createQuery("select em.member from EventMember em", Member.class)
                .getResultList();
    }

}
