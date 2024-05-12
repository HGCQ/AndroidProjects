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
class EventMemberRepositoryTest {

    @Autowired
    EventMemberRepository eventMemberRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 멤버_리스트_조회() throws Exception {
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
        List<Member> memberList = eventMemberRepository.findMemberByEvent();

        // then
        Assertions.assertThat(memberList.size()).isEqualTo(4);
        Assertions.assertThat(memberList).contains(memberA, memberB, memberC, memberD);
    }

    @Test
    public void 회원이_가진_이벤트_리스트_조회() throws Exception {
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
        eventMemberRepository.save(new EventMember(event2.getId(), memberA.getId()));
        eventMemberRepository.save(new EventMember(event1.getId(), memberB.getId()));
        eventMemberRepository.save(new EventMember(event2.getId(), memberC.getId()));
        eventMemberRepository.save(new EventMember(event2.getId(), memberD.getId()));

        // when
        List<Event> eventList = eventMemberRepository.findEventList(memberA);

        // then
        Assertions.assertThat(eventList.size()).isEqualTo(2);
        Assertions.assertThat(eventList).contains(event1, event2);
    }
}