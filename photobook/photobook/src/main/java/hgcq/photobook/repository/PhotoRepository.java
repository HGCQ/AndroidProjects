package hgcq.photobook.repository;

import hgcq.photobook.domain.Photo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoRepository {

    @PersistenceContext
    private final EntityManager em;

    /**
     * 사진 저장
     * @param photo 저장할 사진
     * @return photo_id
     */
    public Long save(Photo photo) {
        if(photo.getId() == null) {
            em.persist(photo);
        } else {
            em.merge(photo);
        }
        return photo.getId();
    }

    /**
     * 사진 삭제
     * @param id photo_id
     * @return photo_id
     */
    public Long delete(Long id) {
        Photo findPhoto = findOne(id);
        em.remove(findPhoto);
        return id;
    }

    /**
     * id 값으로 사진 검색
     * @param id photo_id
     * @return 사진
     */
    public Photo findOne(Long id) {
        return em.find(Photo.class, id);
    }

    /**
     * 이벤트 별 사진 리스트
     * @param eventId event_id
     * @return 이벤트 id에 있는 모든 사진 리스트
     */
    public List<Photo> findAll(Long eventId) {
        return em.createQuery("select p from Photo p where p.event.id = :eventId", Photo.class)
                .setParameter("eventId", eventId)
                .getResultList();
    }
}
