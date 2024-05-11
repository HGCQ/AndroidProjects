package hgcq.photobook.controller;

import hgcq.photobook.domain.Member;
import hgcq.photobook.dto.MemberDTO;
import hgcq.photobook.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberDTO memberDTO, HttpServletResponse response) {
        Member member = new Member(memberDTO.getName(), memberDTO.getEmail(), memberDTO.getPassword());
        Long id = memberService.join(member);

        return ResponseEntity.ok(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO member,
                                   HttpServletResponse response, HttpServletRequest request) {
        Member loginMember = memberService.login(member.getEmail(), member.getPassword());

        if (loginMember != null) {
            MemberDTO loginMemberDTO = new MemberDTO(member.getEmail(), member.getPassword());

            HttpSession session = request.getSession();
            session.setAttribute("member", loginMemberDTO);

            String sessionId = UUID.randomUUID().toString();

            Cookie cookie = new Cookie("session_id", sessionId);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok(loginMemberDTO);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("login fail");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> modify(@RequestBody MemberDTO memberDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDTO existingMember = (MemberDTO) session.getAttribute("member");
            if (existingMember != null) {
                Member findMember = memberService.findOne(memberDTO.getEmail());
                findMember.setName(memberDTO.getName());
                findMember.setPassword(memberDTO.getPassword());
                memberService.update(findMember);
                return ResponseEntity.ok("update success");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("update fail");
    }
}