package hgcq.photobook.controller;

import hgcq.photobook.domain.Member;
import hgcq.photobook.dto.MemberDTO;
import hgcq.photobook.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;
    private final SessionRepository<? extends Session> sessionRepository;

    // 테스트 완료
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberDTO memberDTO) {
        Member member = new Member(memberDTO.getName(), memberDTO.getEmail(), memberDTO.getPassword());
        Long id = memberService.join(member);

        return ResponseEntity.ok(id);
    }

    // 테스트 완료
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO member,
                                   HttpServletRequest request) {
        Member loginMember = memberService.login(member.getEmail(), member.getPassword());

        if (loginMember != null) {
            MemberDTO loginMemberDTO = new MemberDTO(member.getEmail(), member.getPassword());

            HttpSession session = request.getSession();
            session.setAttribute("member", loginMemberDTO);
            log.debug("세션 생성");

            return ResponseEntity.ok(loginMemberDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("로그인 실패");
        }
    }

    // 테스트 완료
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
            log.debug("세션 종료");
            return ResponseEntity.ok("로그아웃 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 중이 아닙니다.");
        }
    }

    // 테스트 완료
    @PostMapping("/update")
    public ResponseEntity<?> modify(@RequestBody MemberDTO memberDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            MemberDTO existingMember = (MemberDTO) session.getAttribute("member");

            if (existingMember != null) {
                Member findMember = memberService.findOne(existingMember.getEmail());
                if (memberDTO.getName() != null) {
                    findMember.setName(memberDTO.getName());
                }
                if (memberDTO.getPassword() != null) {
                    findMember.setPassword(memberDTO.getPassword());
                }
                memberService.update(findMember);
                return ResponseEntity.ok("회원 정보 수정 성공");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원 정보 수정 실패");
    }

    @GetMapping("/duplicate/name")
    public ResponseEntity<?> duplicateName(@RequestParam("name") String name) {
        boolean isNotDuplicate = memberService.duplicateName(name);
        if (isNotDuplicate) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @GetMapping("/duplicate/email")
    public ResponseEntity<?> duplicateEmail(@RequestParam("email") String email) {
        boolean isNotDuplicate = memberService.duplicateEmail(email);
        if (isNotDuplicate) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            MemberDTO existingMember = (MemberDTO) session.getAttribute("member");

            if (existingMember != null) {
                Member findMember = memberService.findOne(existingMember.getEmail());

                if (findMember != null) {
                    MemberDTO memberDTO = new MemberDTO();
                    memberDTO.setName(findMember.getName());
                    memberDTO.setEmail(findMember.getEmail());
                    return ResponseEntity.ok(memberDTO);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 중이 아닙니다.");
    }

    @GetMapping("/islogin")
    public ResponseEntity<?> islogin(HttpServletRequest request) {
        String sessionId = null;
        String decodedString = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("SESSION".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    byte[] decodedBytes = Base64.getDecoder().decode(sessionId);
                    decodedString = new String(decodedBytes);
                    log.debug(sessionId);
                    log.debug(decodedString);
                    break;
                }
            }
        }

        if (sessionId != null) {
            Session session = sessionRepository.findById(decodedString);
            if (session != null) {
                return ResponseEntity.ok(true);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @GetMapping("/friend/search")
    public ResponseEntity<?> searchFriend(@RequestParam("name") String name, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            MemberDTO owner = (MemberDTO) session.getAttribute("member");

            if (owner != null) {
                Member findMember = memberService.findOne(owner.getEmail());

                if (findMember != null) {
                    List<MemberDTO> find = memberService.searchFriend(name, findMember);

                    if (find != null) {
                        return ResponseEntity.ok(find);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("친구 검색 결과 없음");
                    }
                }
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션이 존재하지 않음");
    }

    @PostMapping("/friend/add")
    public ResponseEntity<?> addFriend(@RequestBody MemberDTO memberDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            MemberDTO owner = (MemberDTO) session.getAttribute("member");

            if (owner != null) {
                Member findMember = memberService.findOne(owner.getEmail());

                if (findMember != null) {
                    Member friend = memberService.searchMember(memberDTO.getName());

                    if (friend != null) {
                        memberService.addFriend(findMember, friend);
                        return ResponseEntity.ok("친구 추가 성공");
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("친구 추가 실패");
    }

    // 테스트 완료
    @PostMapping("/friend/delete")
    public ResponseEntity<?> deleteFriend(@RequestBody MemberDTO memberDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            MemberDTO existingMember = (MemberDTO) session.getAttribute("member");

            if (existingMember != null) {
                Member findMember = memberService.findOne(existingMember.getEmail());

                if (findMember != null) {
                    Member friend = memberService.findByName(memberDTO.getName());

                    if (friend != null) {
                        memberService.deleteFriend(findMember, friend);
                        return ResponseEntity.ok("친구 삭제 성공");
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("친구 삭제 실패");
    }

    // 테스트 완료
    @GetMapping("/friend/list")
    public ResponseEntity<?> listFriends(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            MemberDTO existingMember = (MemberDTO) session.getAttribute("member");

            if (existingMember != null) {
                Member findMember = memberService.findOne(existingMember.getEmail());

                if (findMember != null) {
                    List<Member> friends = memberService.friends(findMember);
                    List<MemberDTO> memberDTOS = parseMemberDTOList(friends);
                    return ResponseEntity.ok(memberDTOS);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("친구 리스트 조회 실패");
    }

    // -- 유틸리티 메소드 --

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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