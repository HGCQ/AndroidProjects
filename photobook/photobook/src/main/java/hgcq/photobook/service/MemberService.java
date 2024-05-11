package hgcq.photobook.service;

import hgcq.photobook.domain.Member;
import hgcq.photobook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     *
     * @param member 회원
     * @return 회원 id
     */
    @Transactional
    public Long join(Member member) {
        List<String> emailList = memberRepository.findEmail();

        if (emailList.contains(member.getEmail())) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 로그인
     *
     * @param email    회원 email
     * @param password 회원 password
     * @return 회원
     */
    public Member login(String email, String password) {
        List<String> emails = memberRepository.findEmail();
        if (!emails.contains(email)) {
            return null;
        }
        Member one = memberRepository.findOne(email);
        if (one.getPassword().equals(password)) {
            return one;
        } else {
            return null;
        }
    }

    /**
     * 회원 조회
     *
     * @param email 회원 이메일
     * @return 회원
     */
    public Member findOne(String email) {
        List<String> emails = memberRepository.findEmail();
        if (!emails.contains(email)) {
            return null;
        }
        return memberRepository.findOne(email);
    }

    /**
     * 회원 정보 수정
     *
     * @param member 수정할 회원
     * @return 수정된 회원
     */
    @Transactional
    public Member update(Member member) {
        Member findMember = memberRepository.findOne(member.getEmail());
        if (findMember == null) {
            return null;
        } else {
            findMember.setName(member.getName());
            findMember.setPassword(member.getPassword());
            memberRepository.save(findMember);
            return findMember;
        }
    }
}
