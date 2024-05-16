package hgcq.photobook.controller;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.Member;
import hgcq.photobook.dto.EventDTO;
import hgcq.photobook.dto.MemberDTO;
import hgcq.photobook.dto.MemberInvitationDTO;
import hgcq.photobook.service.EventService;
import hgcq.photobook.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;
    private final MemberService memberService;

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDTO loginMember = (MemberDTO) session.getAttribute("member");
            if (loginMember != null) {
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    Event event = new Event(eventDTO.getName(), LocalDate.parse(eventDTO.getDate()));
                    eventService.createEvent(event, member);
                    return ResponseEntity.ok().body("Event Create Success");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event Create Fail");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteEvent(@RequestBody EventDTO eventDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDTO loginMember = (MemberDTO) session.getAttribute("member");
            if (loginMember != null) {
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    Event event = eventService.searchEventByDate(LocalDate.parse(eventDTO.getDate()), member);
                    if (event != null) {
                        eventService.deleteEvent(event, member);
                        return ResponseEntity.ok().body("Event Delete Success");
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event Delete Fail");
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteEvent(@RequestBody MemberInvitationDTO memberInvitationDTO
            , HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDTO loginMember = (MemberDTO) session.getAttribute("member");
            if (loginMember != null) {
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    EventDTO eventDTO = memberInvitationDTO.getEventDTO();
                    MemberDTO memberDTO = memberInvitationDTO.getMemberDTO();
                    Event event = eventService.searchEventByDate(LocalDate.parse(eventDTO.getDate()), member);
                    Member friend = memberService.findOne(memberDTO.getEmail());
                    if (event != null && friend != null) {
                        eventService.addMemberToEvent(event, friend);
                        return ResponseEntity.ok().body("Event Invite Success");
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event Invite Fail");
    }

    @GetMapping("/findByDate")
    public ResponseEntity<?> findEvent(@RequestParam("date") String date, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDTO loginMember = (MemberDTO) session.getAttribute("member");
            if (loginMember != null) {
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    Event event = eventService.searchEventByDate(LocalDate.parse(date), member);
                    EventDTO eventDTO = parseEventDTO(event);
                    return ResponseEntity.ok(eventDTO);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No events found");
    }

    @GetMapping("/findByName")
    public ResponseEntity<?> findByName(@RequestParam("name") String name, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDTO loginMember = (MemberDTO) session.getAttribute("member");
            if (loginMember != null) {
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    Event event = eventService.searchEventByName(name, member);
                    EventDTO eventDTO = parseEventDTO(event);
                    return ResponseEntity.ok(eventDTO);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No events found");
    }

    // 리스트에 안 담김 -> 고치면 주석 제거
    @PostMapping("/findByFriend")
    public ResponseEntity<?> findByFriend(@RequestBody List<MemberDTO> friends, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDTO loginMember = (MemberDTO) session.getAttribute("member");
            if (loginMember != null) {
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    List<Member> friendList = new ArrayList<>();
                    for (MemberDTO friendDTO : friends) {
                        friendList.add(memberService.findOne(friendDTO.getEmail()));
                    }
                    List<Event> events = eventService.searchEventsByFriend(member, friendList);
                    List<EventDTO> eventDTOS = parseEventDTOList(events);
                    return ResponseEntity.ok(eventDTOS);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No events found");
    }

    @GetMapping("/eventList")
    public ResponseEntity<?> eventList(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDTO loginMember = (MemberDTO) session.getAttribute("member");
            if (loginMember != null) {
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    List<Event> events = eventService.findEvents(member);
                    List<EventDTO> eventDTOS = parseEventDTOList(events);
                    return ResponseEntity.ok(eventDTOS);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No events found");
    }

    @GetMapping("/memberList")
    public ResponseEntity<?> memberList(@RequestParam("date") String date, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDTO loginMember = (MemberDTO) session.getAttribute("member");
            if (loginMember != null) {
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    LocalDate eventDate = LocalDate.parse(date);
                    Event findEvent = eventService.searchEventByDate(eventDate, member);
                    List<Event> events = eventService.findEvents(member);
                    if (events.contains(findEvent)) {
                        List<Member> members = eventService.findMembers(findEvent);
                        List<MemberDTO> memberDTOS = parseMemberDTOList(members);
                        return ResponseEntity.ok(memberDTOS);
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No members found");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private EventDTO parseEventDTO(Event event) {
        return new EventDTO(event.getName(), event.getDate().toString(), event.getContent());
    }

    private List<EventDTO> parseEventDTOList(List<Event> events) {
        List<EventDTO> eventDTOList = new ArrayList<>();
        for (Event event : events) {
            eventDTOList.add(parseEventDTO(event));
        }
        return eventDTOList;
    }

    private MemberDTO parseMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setEmail(member.getEmail());
        memberDTO.setName(member.getName());

        return memberDTO;
    }

    private List<MemberDTO> parseMemberDTOList(List<Member> members) {
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (Member member : members) {
            memberDTOList.add(parseMemberDTO(member));
        }
        return memberDTOList;
    }
}