package hgcq.photobook.service;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.Member;
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
class EventServiceTest {

    @Autowired
    EventService eventService;
    @Autowired
    MemberService memberService;

    @Test
    public void 이벤트_생성() throws Exception {
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
        LocalDate now = LocalDate.now();

        Event event1 = new Event("event1", date);
        Event event2 = new Event("event2", now);

        // when
        eventService.createEvent(event1, memberA);
        eventService.createEvent(event2, memberA);

        // then
        List<Event> events = eventService.findEvents(memberA);
        Assertions.assertThat(events.size()).isEqualTo(2);
        Assertions.assertThat(events).contains(event1, event2);
    }

    @Test
    public void 이벤트_삭제() throws Exception {
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

        // when
        eventService.deleteEvent(date2, memberA);
        List<Event> events = eventService.findEvents(memberA);

        // then
        Assertions.assertThat(events.size()).isEqualTo(2);
        Assertions.assertThat(events).contains(event1, event3).doesNotContain(event2);
    }

    @Test
    public void 회원이_가진_이벤트_리스트_조회() throws Exception {
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

        eventService.addMemberToEvent(date, memberB);
        eventService.addMemberToEvent(date, memberC);

        // when
        List<Event> eventsA = eventService.findEvents(memberA);
        List<Event> eventsB = eventService.findEvents(memberB);

        // then
        Assertions.assertThat(eventsA.size()).isEqualTo(3);
        Assertions.assertThat(eventsA).contains(event1, event2, event3);
        Assertions.assertThat(eventsB).contains(event1);
    }

    @Test
    public void 이벤트_회원_조회() throws Exception {
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

        eventService.addMemberToEvent(date, memberB);
        eventService.addMemberToEvent(date, memberC);

        // when
        List<Member> members = eventService.findMembers(event1);

        // then
        Assertions.assertThat(members.size()).isEqualTo(3);
        Assertions.assertThat(members).contains(memberA, memberB, memberC).doesNotContain(memberD);
    }
}