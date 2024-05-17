package hgcq.photobook.service;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.EventMember;
import hgcq.photobook.domain.Member;
import hgcq.photobook.repository.EventMemberRepository;
import hgcq.photobook.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * 이벤트 생성
 * 이벤트 삭제
 * 이벤트 친구 초대
 * 이벤트 내용 수정
 * 이벤트 검색 (날짜, 이름)
 * 이벤트 친구 필터로 검색
 * 이벤트 리스트
 * 회원 리스트
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final EventMemberRepository eventMemberRepository;

    /**
     * 이벤트 생성
     *
     * @param event  이벤트
     * @param member 회원
     */
    @Transactional
    public void createEvent(Event event, Member member) {
        if (member == null || member.getId() == null) {
            log.error("이벤트 생성 실패 : 회원이 존재하지 않음");
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        List<LocalDate> events = eventMemberRepository.findEventsToDate(member);

        if (events.isEmpty() || !events.contains(event.getDate())) {
            Event newEvent = new Event(event.getName(), event.getDate(), member);
            eventRepository.save(newEvent);
            eventMemberRepository.save(new EventMember(newEvent.getId(), member.getId()));
            log.debug("이벤트 생성 성공");
        } else {
            log.error("이벤트 생성 실패 : 이미 존재하는 날짜임");
            throw new IllegalArgumentException("이미 존재하는 날짜입니다.");
        }
    }

    /**
     * 이벤트 삭제
     *
     * @param event  이벤트
     * @param member 회원
     */
    @Transactional
    public void deleteEvent(Event event, Member member) {
        if (member == null || member.getId() == null) {
            log.error("이벤트 삭제 실패 : 회원이 존재하지 않음");
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        List<Event> events = findEvents(member);

        if (events.contains(event)) {
            // 삭제하려는 사람과 소유자 같은 상황
            if (Objects.equals(event.getMember().getId(), member.getId())) {
                eventMemberRepository.deleteAll(event);
                eventRepository.delete(event);
            }
            // 다른 상황
            else {
                EventMember find = eventMemberRepository.findOne(event, member);
                eventMemberRepository.delete(find);
            }

            log.debug("이벤트 삭제 성공");
        } else {
            log.error("이벤트 삭제 실패 : 이벤트가 존재하지 않음");
            throw new IllegalArgumentException("존재하지 않는 이벤트입니다.");
        }
    }

    /**
     * 이벤트에 친구 초대
     *
     * @param event  이벤트
     * @param friend 친구
     */
    @Transactional
    public void addMemberToEvent(Event event, Member friend) {
        if (friend == null || friend.getId() == null) {
            log.error("친구 초대 실패 : 친구가 존재하지 않음");
            throw new IllegalArgumentException("친구가 존재하지 않습니다.");
        }

        log.debug("친구 초대 성공");
        eventMemberRepository.save(new EventMember(event.getId(), friend.getId()));
    }

    /**
     * 이벤트 내용 설정
     *
     * @param event  이벤트
     * @param member 회원
     * @return 내용
     */
    @Transactional
    public String updateContent(LocalDate date, Member member, String content) {
        if (member == null || member.getId() == null) {
            log.error("내용 설정 실패 : 회원이 존재하지 않음");
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        Event findEvent = searchEventByDate(date, member);

        if (findEvent == null) {
            log.error("내용 설정 실패 : 이벤트가 존재하지 않음");
            throw new IllegalArgumentException("이벤트가 존재하지 않습니다.");
        }

        if (content == null) {
            log.error("내용 설정 실패 : 내용이 존재하지 않음");
            throw new IllegalArgumentException("내용이 존재하지 않습니다.");
        }

        findEvent.setContent(content);
        eventRepository.save(findEvent);

        log.debug("내용 설정 성공");
        return findEvent.getContent();
    }

    /**
     * 이벤트 날짜로 검색
     *
     * @param date   날짜
     * @param member 회원
     * @return 이벤트
     */
    public Event searchEventByDate(LocalDate date, Member member) {
        if (member == null || member.getId() == null) {
            log.error("이벤트 검색 실패 : 회원이 존재하지 않음");
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        Event findEvent = eventMemberRepository.findEventByDate(date, member);

        if (findEvent == null) {
            log.error("이벤트 검색 실패 : 이벤트가 존재하지 않음");
            throw new IllegalArgumentException("이벤트가 존재하지 않습니다.");
        }

        log.debug("이벤트 검색 성공");
        return findEvent;
    }

    /**
     * 이름으로 검색
     *
     * @param name   이름
     * @param member 회원
     * @return 이벤트 리스트
     */
    public List<Event> searchEventByName(String name, Member member) {
        if (member == null || member.getId() == null) {
            log.error("이벤트 검색 실패 : 회원이 존재하지 않음");
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        List<Event> findEvents = eventMemberRepository.findEventByName(name, member);

        if (findEvents == null) {
            log.error("이벤트 검색 실패 : 이벤트가 존재하지 않음");
            throw new IllegalArgumentException("이벤트가 존재하지 않습니다.");
        }

        log.debug("이벤트 검색 성공");
        return findEvents;
    }

    /**
     * 회원을 친구 필터로 검색
     *
     * @param member  회원
     * @param friends 친구들
     * @return 이벤트 리스트
     */
    public List<Event> searchEventsByFriend(Member member, List<Member> friends) {
        if (member == null || member.getId() == null) {
            log.error("이벤트 필터로 검색 실패 : 회원이 존재하지 않음");
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        for (Member friend : friends) {
            if (friend == null || friend.getId() == null) {
                log.error("이벤트 필터로 검색 실패 : 친구가 존재하지 않음");
                throw new IllegalArgumentException("친구가 존재하지 않습니다.");
            }
        }

        log.debug("이벤트 필터로 검색 성공");
        return eventMemberRepository.searchByFriends(member, friends);
    }

    /**
     * 이벤트 리스트 조회
     *
     * @param member 회원
     * @return 이벤트 리스트
     */
    public List<Event> findEvents(Member member) {
        if (member == null || member.getId() == null) {
            log.error("이벤트 리스트 조회 실패 : 회원이 존재하지 않음");
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        log.debug("이벤트 리스트 조회 성공");
        return eventMemberRepository.findEvents(member);
    }

    /**
     * 회원 리스트 조회
     *
     * @param event 이벤트
     * @return 회원 리스트
     */
    public List<Member> findMembers(Event event) {
        if (event == null || event.getId() == null) {
            log.error("이벤트 회원 리스트 조회 실패 : 이벤트가 존재하지 않음");
            throw new IllegalArgumentException("이벤트가 존재하지 않습니다.");
        }

        log.debug("회원 리스트 조회 성공");
        return eventMemberRepository.findMembers(event);
    }
}
