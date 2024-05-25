package hgcq.photobook.repository;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.EventMember;
import hgcq.photobook.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 이벤트 친구 초대
 * 이벤트 친구 탈퇴
 * 이벤트 조회 (날짜, 이름)
 * 이벤트 친구 별 검색
 * 회원 리스트 조회
 * 이벤트 리스트 조회
 */

@Repository
@RequiredArgsConstructor
public class EventMemberRepository {

    @PersistenceContext
    private final EntityManager em;

    /**
     * 이벤트 친구 초대
     *
     * @param eventMember 이벤트와 회원
     */
    public void save(EventMember eventMember) {
        em.persist(eventMember);
    }

    /**
     * 이벤트 친구 탈퇴
     *
     * @param eventMember 이벤트와 회원
     */
    public void delete(EventMember eventMember) {
        em.remove(eventMember);
    }

    /**
     * 이벤트멤버 전체 삭제
     *
     * @param event 이벤트
     */
    public void deleteAll(Event event) {
        em.createQuery("delete from EventMember em where em.event = :event")
                .setParameter("event", event)
                .executeUpdate();
    }

    /**
     * 이벤트 날짜로 조회
     *
     * @param date   날짜
     * @param member 회원
     * @return 이벤트
     */
    public Event findEventByDate(LocalDate date, Member member) {
        try {
            return em.createQuery(
                            "select em.event from EventMember em where em.event.date = :date and em.member = :member", Event.class)
                    .setParameter("date", date)
                    .setParameter("member", member)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 이벤트 이름으로 조회
     *
     * @param name   이벤트 이름
     * @param member 회원
     * @return 이벤트 리스트
     */
    public List<Event> findEventByName(String name, Member member) {
        return em.createQuery("select em.event from EventMember em where em.event.name LIKE :name and em.member = :member", Event.class)
                .setParameter("name", "%" + name + "%")
                .setParameter("member", member)
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
     * 친구 필터로 이벤트 리스트 검색
     *
     * @param member  회원
     * @param friends 친구들
     * @return 이벤트 리스트
     */
    public List<Event> searchByFriends(Member member, List<Member> friends) {
        return em.createQuery("select em.event from EventMember em where em.member = :member and em.member in (:friends)", Event.class)
                .setParameter("member", member)
                .setParameter("friends", friends)
                .getResultList();
    }

    /**
     * 이벤트 회원 리스트 조회
     *
     * @param event 이벤트
     * @return 회원 리스트
     */
    public List<Member> findMembers(Event event) {
        return em.createQuery("select em.member from EventMember em where em.event = :event", Member.class)
                .setParameter("event", event)
                .getResultList();
    }

    /**
     * 회원이 가진 이벤트 리스트 조회
     *
     * @param member 회원
     * @return 이벤트 리스트
     */
    public List<Event> findEvents(Member member) {
        return em.createQuery("select em.event from EventMember em where em.member = :member", Event.class)
                .setParameter("member", member)
                .getResultList();
    }

    /**
     * 회원이 가진 이벤트 날짜 리스트 조회
     *
     * @param member 회원
     * @return 이벤트 날짜 리스트
     */
    public List<LocalDate> findEventsToDate(Member member) {
        return em.createQuery("select em.event.date from EventMember em where em.member = :member", LocalDate.class)
                .setParameter("member", member)
                .getResultList();
    }


}
