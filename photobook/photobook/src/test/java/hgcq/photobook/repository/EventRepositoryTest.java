package hgcq.photobook.repository;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.EventMember;
import hgcq.photobook.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventMemberRepository eventMemberRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 이벤트_생성() throws Exception {
        // given
        LocalDate now = LocalDate.now();
        Event event = new Event("event1", now);

        // when
        eventRepository.save(event);

        // then
        Event findEvent = eventRepository.findEventByDate(now);
        Assertions.assertThat(findEvent).isEqualTo(event);
    }

    @Test
    public void 이벤트_조회() throws Exception {
        // given
        LocalDate date = LocalDate.of(2024, 5, 10);
        LocalDate now = LocalDate.now();

        Event event1 = new Event("event1", date);
        Event event2 = new Event("event2", now);

        eventRepository.save(event1);
        eventRepository.save(event2);

        // when
        Event findEvent = eventRepository.findEventByDate(date);

        // then
        Assertions.assertThat(findEvent).isEqualTo(event1);
        Assertions.assertThat(findEvent).isNotEqualTo(event2);
        Assertions.assertThat(findEvent.getDate()).isEqualTo("2024-05-10");
    }

    @Test
    public void 이벤트_삭제() throws Exception {
        // given
        LocalDate date = LocalDate.of(2024, 5, 10);
        LocalDate now = LocalDate.now();

        Event event1 = new Event("event1", date);
        Event event2 = new Event("event2", now);

        eventRepository.save(event1);
        eventRepository.save(event2);

        // when
        Event findEvent = eventRepository.findEventByDate(date);
        Event deleteEvent = eventRepository.delete(findEvent);

        // then
        Assertions.assertThat(deleteEvent).isEqualTo(event1);
    }

    @Test
    public void 이벤트_리스트() throws Exception {
        // given
        LocalDate date = LocalDate.of(2024, 5, 10);
        LocalDate now = LocalDate.now();

        Event event1 = new Event("event1", date);
        Event event2 = new Event("event2", now);

        eventRepository.save(event1);
        eventRepository.save(event2);

        // when
        List<LocalDate> eventList = eventRepository.findEventList();

        // then
        Assertions.assertThat(eventList.size()).isEqualTo(2);
        Assertions.assertThat(eventList).contains(date, now);
    }

    @Test
    public void 이벤트_회원_리스트_조회() throws Exception {
        // given
        LocalDate date = LocalDate.of(2024, 5, 10);
        LocalDate now = LocalDate.now();

        Event event1 = new Event("event1", date);
        Event event2 = new Event("event2", now);

        eventRepository.save(event1);
        eventRepository.save(event2);

        Member memberA = new Member("A", "aaa@naver.com", "a1234");
        Member memberB = new Member("B", "bbb@naver.com", "b1234");
        Member memberC = new Member("C", "ccc@naver.com", "c1234");
        Member memberD = new Member("D", "ddd@naver.com", "d1234");

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);
        memberRepository.save(memberD);

        eventMemberRepository.save(new EventMember(event1.getId(), memberA.getId()));
        eventMemberRepository.save(new EventMember(event1.getId(), memberB.getId()));
        eventMemberRepository.save(new EventMember(event2.getId(), memberC.getId()));
        eventMemberRepository.save(new EventMember(event2.getId(), memberD.getId()));
        // when
        List<Member> memberList = eventMemberRepository.findMemberByEvent(event1);

        // then
        Assertions.assertThat(memberList.size()).isEqualTo(2);
        Assertions.assertThat(memberList).contains(memberA, memberB);
        Assertions.assertThat(memberList).doesNotContain(memberC, memberD);
    }
}