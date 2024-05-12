package hgcq.photobook.repository;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventRepository {

    @PersistenceContext
    private final EntityManager em;

    /**
     * 이벤트 저장
     *
     * @param event 이벤트
     * @return 이벤트 id
     */
    public Long save(Event event) {
        if (event.getId() == null) {
            em.persist(event);
        } else {
            em.merge(event);
        }
        return event.getId();
    }

    /**
     * 이벤트 날짜로 조회
     *
     * @param date 이벤트 날짜
     * @return 이벤트
     */
    public Event findEventByDate(LocalDate date) {
        return em.createQuery("select e from Event e where e.date = :date", Event.class)
                .setParameter("date", date)
                .getSingleResult();
    }

    /**
     * 이벤트 id로 조회
     *
     * @param id 이벤트 id
     * @return 이벤트
     */
    public Event findEventById(Long id) {
        return em.find(Event.class, id);
    }

    /**
     * 이벤트 삭제
     *
     * @param event 이벤트
     * @return 삭제한 이벤트
     */
    public Event delete(Event event) {
        em.remove(event);
        return event;
    }

    /**
     * 이벤트 리스트 가져오기
     *
     * @return 이벤트 리스트
     */
    public List<LocalDate> findEventList() {
        return em.createQuery("select e.date from Event e", LocalDate.class)
                .getResultList();
    }
}
