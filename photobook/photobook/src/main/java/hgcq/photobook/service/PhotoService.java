package hgcq.photobook.service;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.Photo;
import hgcq.photobook.repository.EventRepository;
import hgcq.photobook.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final EventRepository eventRepository;

    /**
     * 사진 업로드
     *
     * @param photo 업로드할 사진
     */
    @Transactional
    public void uploadPhoto(Photo photo) {
        if (photo.getImage() != null && photo.getImageName() != null) {
            Event event = photo.getEvent();
            Event findEvent = eventRepository.findEventByDate(event.getDate());
            if (findEvent != null) {
                photoRepository.save(photo);
            }
        } else {
            throw new IllegalStateException("업로드 실패");
        }
    }


    /**
     * 사진 삭제
     *
     * @param imageName 사진 이름
     * @param event     이벤트
     */
    @Transactional
    public void deletePhoto(String imageName, Event event) {
        photoRepository.delete(imageName, event);
    }

    /**
     * 사진 하나 조회
     *
     * @param imageName 사진 이름
     * @param event     이벤트
     * @return 사진
     */
    public Photo getPhoto(String imageName, Event event) {
        return photoRepository.findOne(imageName, event);
    }

    /**
     * 이벤트 사진 전체 조회
     *
     * @param event 이벤트
     * @return 사진 리스트
     */
    public List<Photo> photoList(Event event) {
        return photoRepository.findAll(event);
    }
}
