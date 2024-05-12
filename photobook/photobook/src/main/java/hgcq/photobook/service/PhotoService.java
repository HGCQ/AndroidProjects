package hgcq.photobook.service;

import hgcq.photobook.domain.Photo;
import hgcq.photobook.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;

    /**
     * 사진 업로드
     * @param photo 업로드할 사진
     * @return photo_id
     */
    @Transactional
    public Long uploadPhoto(Photo photo) {
        if(photo.getImage() != null && photo.getImageName() == null) {
            photoRepository.save(photo);
        } else {
            throw new IllegalStateException("잘못된 접근입니다.");
        }
        return photo.getId();
    }

    /**
     * 사진 삭제
     * @param id photo_id
     * @return photo_id
     */
    public Long deletePhoto(Long id) {
        photoRepository.delete(id);
        return id;
    }

    /**
     * 이벤트 사진 리스트
     * @param eventId event_id
     * @return 이벤트마다 사진 리스트
     */
    public List<Photo> photoList(Long eventId) {
        return photoRepository.findAll(eventId);
    }
}
