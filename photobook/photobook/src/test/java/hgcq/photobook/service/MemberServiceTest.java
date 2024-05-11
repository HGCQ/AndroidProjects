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

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원가입() {
        Member member = new Member("이동현", "ehdgus2580@naver.com", "qwer1234");
        Long memberId = memberService.join(member);

        Member findMember = memberRepository.findOne("ehdgus2580@naver.com");

        assertEquals(member, findMember);
    }

    @Test
    void 중복검사() {
        Member memberA = new Member("이동현", "ehdgus2580@naver.com", "qwer1234");
        memberService.join(memberA);

        try {
            Member memberB = new Member("전경섭", "ehdgus2580@naver.com", "asdf1234");
            memberService.join(memberB);
        } catch (IllegalStateException e) {
            return;
        }

        Assertions.fail("예외 발생");
    }

    @Test
    public void 로그인() throws Exception {
        // given
        Member member = new Member("A", "aaa@naver.com", "1234");
        memberService.join(member);

        // when
        Member loginMember = memberService.login("aaa@naver.com", "1234");

        // then
        Assertions.assertThat(loginMember).isNotNull();
        Assertions.assertThat(loginMember.getName()).isEqualTo("A");
        Assertions.assertThat(loginMember.getEmail()).isEqualTo("aaa@naver.com");
        Assertions.assertThat(loginMember.getPassword()).isEqualTo("1234");
        Assertions.assertThat(member).isEqualTo(loginMember);
    }

    @Test
    public void 로그인_실패() throws Exception {
        // given
        Member member = new Member("A", "aaa@naver.com", "1234");
        memberService.join(member);

        // when
        Member login1 = memberService.login("bbb@naver.com", "1234");
        Member login2 = memberService.login("aaa@naver.com", "1111");

        // then
        Assertions.assertThat(login1).isNull();
        Assertions.assertThat(login2).isNull();
    }

    @Test
    public void 업데이트() throws Exception {
        // given
        Member member = new Member("이동현", "ehdgus2580@naver.com", "qwer1234");
        Member member1 = new Member("A", "aaa@naver.com", "1");
        Member member2 = new Member("B", "bbb@naver.com", "2");
        Member member3 = new Member("C", "ccc@naver.com", "3");

        memberService.join(member);
        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);

        // when
        Member updateMember = new Member("수정", "ehdgus2580@naver.com", "qw1");
        Member update = memberService.update(updateMember);

        // then
        Assertions.assertThat(update.getName()).isEqualTo("수정");
    }

}