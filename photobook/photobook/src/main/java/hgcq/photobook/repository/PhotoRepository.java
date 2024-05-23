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
     * @param photo 사진
     * @return 사진 id
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
     * @param photo 사진
     */
    public void delete(Photo photo) {
        em.remove(photo);
    }

    /**
     * 사진 경로로 검색
     *
     * @param path 경로
     * @return 사진
     */
    public Photo findByPath(String path) {
        return em.createQuery("select p from Photo p where p.path = :path", Photo.class)
                .setParameter("path", path)
                .getSingleResult();
    }

    /**
     * 사진 하나 검색
     *
     * @param imageName 사진 이름
     * @return 사진
     */
    public Photo findOne(String imageName, Event event) {
        return em.createQuery("select p from Photo p where p.imageName = :imageName and p.event = :event", Photo.class)
                .setParameter("imageName", imageName)
                .setParameter("event", event)
                .getSingleResult();
    }

    /**
     * 사진 리스트 조회
     *
     * @param event 이벤트
     * @return 사진 리스트
     */
    public List<String> findAll(Event event) {
        return em.createQuery("select p.path from Photo p where p.event = :event", String.class)
                .setParameter("event", event)
                .getResultList();
    }

    /**
     * 사진 이름 리스트 조회
     *
     * @param event 이벤트
     * @return 사진 이름 리스트
     */
    public List<String> findImageNames(Event event) {
        return em.createQuery("select p.imageName from Photo p where p.event = :event", String.class)
                .setParameter("event", event)
                .getResultList();
    }
}
