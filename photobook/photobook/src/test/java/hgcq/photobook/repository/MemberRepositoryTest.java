package hgcq.photobook.repository;

import hgcq.photobook.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    public void save() throws Exception {
        // given
        Member member = new Member("A", "abc@gmail.com", "qwer1234");

        // when
        Long saveId = memberRepository.save(member);

        // then
        assertThat(saveId).isEqualTo(member.getId());
    }

    @Test
    public void findOne() throws Exception {
        // given
        Member member = new Member("A", "abc@gmail.com", "qwer1234");
        Long memberId = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findOne(memberId);

        // then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void findAll() throws Exception {
        // given
        Member member1 = new Member("A", "abc@gmail.com", "qwer1234");
        Member member2 = new Member("B", "abc@gmail.com", "qwer1234");
        Member member3 = new Member("C", "abc@gmail.com", "qwer1234");
        Member member4 = new Member("D", "abc@gmail.com", "qwer1234");
        Member member5 = new Member("E", "abc@gmail.com", "qwer1234");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // when
        List<Member> findMemberList = memberRepository.findAll();

        // then
        assertThat(findMemberList.size()).isEqualTo(4);
        assertThat(findMemberList).contains(member1, member2, member3, member4);
        assertThat(findMemberList).doesNotContain(member5);
    }

    @Test
    public void findEmail() throws Exception {
        // given
        Member member1 = new Member("A", "aaa@gmail.com", "qwer1234");
        Member member2 = new Member("B", "bbb@gmail.com", "qwer1234");
        Member member3 = new Member("C", "ccc@gmail.com", "qwer1234");
        Member member4 = new Member("D", "ddd@gmail.com", "qwer1234");
        Member member5 = new Member("E", "eee@gmail.com", "qwer1234");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        String email1 = member1.getEmail();
        String email2 = member2.getEmail();
        String email3 = member3.getEmail();
        String email4 = member4.getEmail();
        String email5 = member5.getEmail();

        // when
        List<String> emailList = memberRepository.findEmail();

        // then
        assertThat(emailList.size()).isEqualTo(4);
        assertThat(emailList).contains(email1, email2, email3, email4);
        assertThat(emailList).doesNotContain(email5);
    }
}