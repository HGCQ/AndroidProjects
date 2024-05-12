package hgcq.photobook.controller;

import hgcq.photobook.domain.Event;
import hgcq.photobook.domain.Member;
import hgcq.photobook.dto.EventDTO;
import hgcq.photobook.dto.MemberDTO;
import hgcq.photobook.service.EventService;
import hgcq.photobook.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
                Event event = new Event(eventDTO.getName(), eventDTO.getDate());
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    eventService.createEvent(event, member);
                    return ResponseEntity.ok().body("Event Create Success");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Event Create Fail");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteEvent(@RequestBody EventDTO eventDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDTO loginMember = (MemberDTO) session.getAttribute("member");
            if (loginMember != null) {
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    Event event = eventService.getEvent(eventDTO.getDate(), member);
                    if (event != null) {
                        eventService.deleteEvent(event.getDate(), member);
                        return ResponseEntity.ok().body("Event Delete Success");
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Event Delete Fail");
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteEvent(@RequestBody EventDTO eventDTO, @RequestBody MemberDTO memberDTO
            , HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            MemberDTO loginMember = (MemberDTO) session.getAttribute("member");
            if (loginMember != null) {
                Member member = memberService.findOne(loginMember.getEmail());
                if (member != null) {
                    Event event = eventService.getEvent(eventDTO.getDate(), member);
                    Member friend = memberService.findOne(memberDTO.getEmail());
                    if (event != null && friend != null) {
                        eventService.addMemberToEvent(event.getDate(), friend);
                        return ResponseEntity.ok().body("Event Invite Success");
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Event Invite Fail");
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
                    return ResponseEntity.ok(events);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No events found");
    }
}
