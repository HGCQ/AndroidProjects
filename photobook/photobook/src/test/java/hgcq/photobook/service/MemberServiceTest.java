package hgcq.photobook.service;

import hgcq.photobook.domain.Member;
import hgcq.photobook.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    
    @Test
    void 회원가입() {
        Member member = new Member("이동현","ehdgus2580@naver.com","qwer1234");
        Long memberId = memberService.join(member);

        Member findMember = memberRepository.findOne(memberId);

        assertEquals(member,findMember);
    }

    @Test
    void 중복검사() {
        Member memberA = new Member("이동현","ehdgus2580@naver.com","qwer1234");
        memberService.join(memberA);
       
        try {
            Member memberB = new Member("전경섭","ehdgus2580@naver.com","asdf1234");
            memberService.join(memberB);
        } catch (IllegalStateException e){
            return;
        }

        Assertions.fail("예외 발생");
    }

}