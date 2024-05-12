package hgcq.photobook.service;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.Member;
import hgcq.photobook.domain.Photo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class PhotoServiceTest {

    @Autowired
    PhotoService photoService;
    @Autowired
    MemberService memberService;
    @Autowired
    EventService eventService;

    @Test
    public void 사진_업로드() throws Exception {
        // given
        Member memberA = new Member("A", "aaa@naver.com", "a1234");
        Member memberB = new Member("B", "bbb@naver.com", "b1234");
        Member memberC = new Member("C", "ccc@naver.com", "c1234");
        Member memberD = new Member("D", "ddd@naver.com", "d1234");

        memberService.join(memberA);
        memberService.join(memberB);
        memberService.join(memberC);
        memberService.join(memberD);

        LocalDate date = LocalDate.of(2024, 5, 10);
        LocalDate date2 = LocalDate.of(2024, 5, 11);
        LocalDate now = LocalDate.now();

        Event event1 = new Event("event1", date);
        Event event2 = new Event("event2", date2);
        Event event3 = new Event("event3", now);

        eventService.createEvent(event1, memberA);
        eventService.createEvent(event2, memberA);
        eventService.createEvent(event3, memberA);
        eventService.addMemberToEvent(event1.getDate(), memberB);
        eventService.addMemberToEvent(event2.getDate(), memberC);
        eventService.addMemberToEvent(event3.getDate(), memberD);

        // when
        Photo photoA = new Photo("photoA", new byte[]{1, 2, 3}, event1);
        Photo photoB = new Photo("photoB", new byte[]{1, 2, 3}, event1);
        Photo photoC = new Photo("photoC", new byte[]{1, 2, 3}, event2);
        Photo photoD = new Photo("photoD", new byte[]{1, 2, 3}, event3);

        photoService.uploadPhoto(photoA);
        photoService.uploadPhoto(photoB);
        photoService.uploadPhoto(photoC);
        photoService.uploadPhoto(photoD);

        // then
        List<Photo> photos1 = photoService.photoList(event1);
        Assertions.assertThat(photos1).contains(photoA, photoB).doesNotContain(photoC, photoD);
    }

    @Test
    public void 사진_삭제() throws Exception {
        // given
        Member memberA = new Member("A", "aaa@naver.com", "a1234");
        Member memberB = new Member("B", "bbb@naver.com", "b1234");
        Member memberC = new Member("C", "ccc@naver.com", "c1234");
        Member memberD = new Member("D", "ddd@naver.com", "d1234");

        memberService.join(memberA);
        memberService.join(memberB);
        memberService.join(memberC);
        memberService.join(memberD);

        LocalDate date = LocalDate.of(2024, 5, 10);
        LocalDate date2 = LocalDate.of(2024, 5, 11);
        LocalDate now = LocalDate.now();

        Event event1 = new Event("event1", date);
        Event event2 = new Event("event2", date2);
        Event event3 = new Event("event3", now);

        eventService.createEvent(event1, memberA);
        eventService.createEvent(event2, memberA);
        eventService.createEvent(event3, memberA);
        eventService.addMemberToEvent(event1.getDate(), memberB);
        eventService.addMemberToEvent(event2.getDate(), memberC);
        eventService.addMemberToEvent(event3.getDate(), memberD);

        Photo photoA = new Photo("photoA", new byte[]{1, 2, 3}, event1);
        Photo photoB = new Photo("photoB", new byte[]{1, 2, 3}, event1);
        Photo photoC = new Photo("photoC", new byte[]{1, 2, 3}, event2);
        Photo photoD = new Photo("photoD", new byte[]{1, 2, 3}, event3);

        photoService.uploadPhoto(photoA);
        photoService.uploadPhoto(photoB);
        photoService.uploadPhoto(photoC);
        photoService.uploadPhoto(photoD);

        // when
        photoService.deletePhoto("photoA", event1);

        // then
        List<Photo> photos = photoService.photoList(event1);
        Assertions.assertThat(photos).contains(photoB).doesNotContain(photoA, photoC, photoD);

    }
}