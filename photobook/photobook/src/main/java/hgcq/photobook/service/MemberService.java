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

    @Transactional
    public Long join(Member member){
        List<String> emailList = memberRepository.findEmail();

        if(emailList.contains(member.getEmail())){
             throw new IllegalStateException("이미 존재하는 아이디입니다.");

        }

        memberRepository.save(member);
        return member.getId();
    }
}
