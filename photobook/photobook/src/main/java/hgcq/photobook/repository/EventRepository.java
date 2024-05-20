package hgcq.photobook.repository;

import hgcq.photobook.domain.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 이벤트 생성
 * 이벤트 삭제 (소유자만)
 */

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
     * 이벤트 삭제
     *
     * @param event 이벤트
     * @return 삭제한 이벤트
     */
    public Event delete(Event event) {
        em.remove(event);
        return event;
    }

}
