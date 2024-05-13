package hgcq.photobook.service;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.EventMember;
import hgcq.photobook.domain.Member;
import hgcq.photobook.repository.EventMemberRepository;
import hgcq.photobook.repository.EventRepository;
import hgcq.photobook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMemberRepository eventMemberRepository;
    private final MemberRepository memberRepository;

    /**
     * 이벤트 생성
     *
     * @param event  이벤트
     * @param member 회원
     */
    @Transactional
    public void createEvent(Event event, Member member) {
        List<LocalDate> dateList = eventRepository.findEventList();
        if (!dateList.isEmpty() && dateList.contains(event.getDate())) {
            throw new IllegalStateException("이미 존재하는 이벤트 날짜입니다.");
        } else {
            eventRepository.save(event);
            eventMemberRepository.save(new EventMember(event.getId(), member.getId()));
        }
    }

    /**
     * 이벤트 삭제
     *
     * @param date   이벤트 날짜
     * @param member 회원
     */
    @Transactional
    public void deleteEvent(LocalDate date, Member member) {
        Event findEvent = eventRepository.findEventByDate(date);
        if (findEvent != null) {
            List<EventMember> eventMemberList = eventMemberRepository.findByEvent(findEvent);
            List<Event> eventList = eventMemberRepository.findEventList(member);

            if (eventList.contains(findEvent)) {
                eventMemberRepository.deleteAll(eventMemberList);
                eventRepository.delete(findEvent);
            }
        }
    }

    /**
     * 이벤트 조회
     *
     * @param date   날짜
     * @param member 회원
     * @return 이벤트
     */
    public Event getEvent(LocalDate date, Member member) {
        Event findEvent = eventRepository.findEventByDate(date);
        Member findMember = memberRepository.findOne(member.getEmail());
        if (findMember != null && findEvent != null) {
            EventMember relate = eventMemberRepository.findOne(findEvent, findMember);
            if (relate != null) {
                return findEvent;
            }
        }
        return null;
    }

    /**
     * 이벤트에 회원 추가
     *
     * @param date   이벤트 날짜
     * @param member 회원
     */
    public void addMemberToEvent(LocalDate date, Member member) {
        Event findEvent = eventRepository.findEventByDate(date);
        if (findEvent != null && member != null) {
            eventMemberRepository.save(new EventMember(findEvent.getId(), member.getId()));
        }
    }

    /**
     * 회원이 가진 이벤트 리스트 전체 조회
     *
     * @param member 검색할 회원
     * @return 이벤트 리스트
     */
    public List<Event> findEvents(Member member) {
        List<Member> memberList = eventMemberRepository.findMemberByEvent();
        if (member == null || !memberList.contains(member)) {
            return new ArrayList<>();
        }
        return eventMemberRepository.findEventList(member);
    }

    /**
     * 이벤트의 회원 리스트 전체 조회
     *
     * @param event 이벤트
     * @return 회원 리스트
     */
    public List<Member> findMembers(Event event) {
        if (event == null) {
            return new ArrayList<>();
        }
        return eventMemberRepository.findMemberByEvent(event);
    }
}
