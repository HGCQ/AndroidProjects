package hgcq.photobook.service;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.Member;
import hgcq.photobook.domain.Photo;
import hgcq.photobook.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사진 업로드
 * 사진 삭제
 * 사진 검색
 * 사진 리스트 검색
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoService {

    private static final Logger log = LoggerFactory.getLogger(PhotoService.class);

    private final PhotoRepository photoRepository;
    private final EventService eventService;
    private final MemberService memberService;

    /**
     * 사진 업로드
     *
     * @param photo  사진
     * @param member 회원
     */
    @Transactional
    public void uploadPhoto(Photo photo, Member member) {
        if (photo.getImageName() == null || photo.getPath() == null) {
            log.error("사진 업로드 실패");
            throw new IllegalArgumentException("잘못된 파일입니다.");
        }

        if (member == null) {
            log.error("사진 업로드 실패");
            throw new IllegalArgumentException("잘못된 파일입니다.");
        }

        Member findMember = memberService.findOne(member.getEmail());

        if (findMember == null) {
            log.error("사진 업로드 실패");
            throw new IllegalArgumentException("회원이 없습니다.");
        }

        Event findEvent = eventService.searchEventByDate(photo.getEvent().getDate(), findMember);

        if (findEvent == null) {
            log.error("사진 업로드 실패");
            throw new IllegalArgumentException("이벤트가 없습니다.");
        }

        photoRepository.save(photo);
        log.debug("사진 업로드 성공");
    }


    @Transactional
    public boolean deletePhoto(String path, Member member) {
        Photo findPhoto = photoRepository.findByPath(path);

        if (findPhoto == null) {
            log.error("사진이 없습니다.");
            throw new IllegalArgumentException("잘못된 파일입니다.");
        }

        photoRepository.delete(findPhoto);
        log.debug("사진 삭제 성공");
        return true;
    }

    /**
     * 사진 검색
     *
     * @param imageName 이미지 이름
     * @param event     이벤트
     * @param member    회원
     * @return 사진
     */
    public Photo findPhoto(String imageName, Event event, Member member) {
        if (imageName == null || event == null || member == null) {
            log.error("사진 조회 실패");
            throw new IllegalArgumentException("잘못된 파일입니다.");
        }

        Member findMember = memberService.findOne(member.getEmail());

        if (findMember == null) {
            log.error("사진 조회 실패");
            throw new IllegalArgumentException("회원이 없습니다.");
        }

        Event findEvent = eventService.searchEventByDate(event.getDate(), findMember);

        if (findEvent == null) {
            log.error("사진 조회 실패");
            throw new IllegalArgumentException("이벤트가 없습니다.");
        }

        List<String> imageNames = photoRepository.findImageNames(findEvent);

        if (!imageNames.contains(imageName)) {
            log.error("사진 조회 실패");
            throw new IllegalArgumentException("사진이 없습니다.");
        }

        log.debug("사진 조회 성공");
        return photoRepository.findOne(imageName, findEvent);
    }

    /**
     * 사진 리스트 검색
     *
     * @param event  이벤트
     * @param member 회원
     * @return 사진 경로 리스트
     */
    public List<String> photoList(Event event, Member member) {
        if (event == null || member == null) {
            log.error("사진 리스트 실패");
            throw new IllegalArgumentException("잘못된 파일입니다.");
        }

        Member findMember = memberService.findOne(member.getEmail());

        if (findMember == null) {
            log.error("사진 리스트 조회 실패");
            throw new IllegalArgumentException("회원이 없습니다.");
        }

        Event findEvent = eventService.searchEventByDate(event.getDate(), findMember);

        if (findEvent == null) {
            log.error("사진 리스트 조회 실패");
            throw new IllegalArgumentException("이벤트가 없습니다.");
        }

        log.debug("사진 리스트 조회 성공");
        return photoRepository.findAll(findEvent);
    }
}
