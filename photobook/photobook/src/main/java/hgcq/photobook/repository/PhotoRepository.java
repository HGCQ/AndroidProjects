package hgcq.photobook.repository;

import hgcq.photobook.domain.Event;
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
     *
     * @param photo 저장할 사진
     * @return photo_id
     */
    public Long save(Photo photo) {
        if (photo.getId() == null) {
            em.persist(photo);
        } else {
            em.merge(photo);
        }
        return photo.getId();
    }


    /**
     * 사진 삭제
     *
     * @param photoName 사진 이름
     * @param event     이벤트
     */
    public void delete(String photoName, Event event) {
        Photo findPhoto = findOne(photoName, event);
        em.remove(findPhoto);
    }


    /**
     * 사진 하나 조회
     *
     * @param photoName 사진 이름
     * @param event     이벤트
     * @return 사진
     */
    public Photo findOne(String photoName, Event event) {
        return em.createQuery("select p from Photo p where imageName = :imageName and event = :event"
                        , Photo.class)
                .setParameter("imageName", photoName)
                .setParameter("event", event)
                .getSingleResult();
    }

    /**
     * 이벤트에 있는 전체 사진 조회
     *
     * @param event 이벤트
     * @return 사진 리스트
     */
    public List<Photo> findAll(Event event) {
        return em.createQuery("select p from Photo p where p.event = :event", Photo.class)
                .setParameter("event", event)
                .getResultList();
    }
}
