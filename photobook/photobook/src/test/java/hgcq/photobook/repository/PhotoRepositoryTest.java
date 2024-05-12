package hgcq.photobook.repository;

import hgcq.photobook.domain.Event;
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
class PhotoRepositoryTest {

    @Autowired PhotoRepository photoRepository;
    @Autowired EventRepository eventRepository;

    @Test
    public void 사진_추가() throws Exception {
        // given
        Event event = new Event("event1", LocalDate.now());
        eventRepository.save(event);

        Photo A = new Photo("a.jpg", new byte[]{1,2,3,4}, event);
        Photo B = new Photo("b.jpg", new byte[]{1,2,3,4}, event);
        Photo C = new Photo("c.jpg", new byte[]{1,2,3,4}, event);

        // when
        Long saveA = photoRepository.save(A);
        Long saveB = photoRepository.save(B);
        Long saveC = photoRepository.save(C);

        photoRepository.save(A);
        photoRepository.save(B);
        photoRepository.save(C);

        // then
        Photo findA = photoRepository.findOne("a.jpg", event);
        Photo findB = photoRepository.findOne("b.jpg", event);
        Photo findC = photoRepository.findOne("c.jpg", event);

        Assertions.assertThat(findA).isEqualTo(A);
        Assertions.assertThat(findB).isEqualTo(B);
        Assertions.assertThat(findC).isEqualTo(C);
    }

    @Test
    public void 사진_삭제() throws Exception {
        // given
        Event event = new Event("event1", LocalDate.now());
        eventRepository.save(event);

        Photo A = new Photo("a.jpg", new byte[]{1,2,3,4}, event);
        Photo B = new Photo("b.jpg", new byte[]{1,2,3,4}, event);
        Photo C = new Photo("c.jpg", new byte[]{1,2,3,4}, event);

        Long saveA = photoRepository.save(A);
        Long saveB = photoRepository.save(B);
        Long saveC = photoRepository.save(C);

        // when
        photoRepository.delete("a.jpg", event);
        List<Photo> all = photoRepository.findAll(event);

        // then
        Assertions.assertThat(all.size()).isEqualTo(2);
        Assertions.assertThat(all).contains(B, C).doesNotContain(A);
    }
}