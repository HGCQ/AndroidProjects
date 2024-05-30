package hgcq.photobook.service;

import hgcq.photobook.domain.Friend;
import hgcq.photobook.domain.Member;
import hgcq.photobook.dto.MemberDTO;
import hgcq.photobook.repository.FriendRepository;
import hgcq.photobook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    @Transactional
    @Synchronized
    public Long join(Member member) {
        List<String> emailList = memberRepository.findEmail();
        List<String> nameList = memberRepository.findName();

        if (emailList.contains(member.getEmail())) {
            log.error("회원 가입 실패 : 이미 존재하는 아이디");
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (nameList.contains(member.getName())) {
            log.error("회원 가입 실패 : 이미 존재하는 닉네임");
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        memberRepository.save(member);
        log.debug("회원 가입 성공");
        return member.getId();
    }


    public Member login(String email, String password) {
        List<String> emails = memberRepository.findEmail();

        if (!emails.contains(email)) {
            log.error("로그인 실패 : 존재하지 않는 아이디");
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }

        Member find = memberRepository.findOne(email);

        if (find.getPassword().equals(password)) {
            log.debug("로그인 성공");
            return find;
        } else {
            log.error("로그인 실패 : 비밀번호 틀림");
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
    }

    public Member findOne(String email) {
        List<String> emails = memberRepository.findEmail();

        if (!emails.contains(email)) {
            log.error("조회 실패 : 존재하지 않는 아이디");
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }

        log.debug("조회 성공");
        return memberRepository.findOne(email);
    }

    public Member findByName(String name) {
        List<Member> find = memberRepository.findByName(name);

        if (find.isEmpty()) {
            log.error("조회 실패 : 존재하지 않는 회원");
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        return find.get(0);
    }

    /**
     * 회원 정보 수정
     *
     * @param member 수정할 회원
     */
    @Transactional
    public void update(Member member) {
        Member findMember = memberRepository.findOne(member.getEmail());

        findMember.setName(member.getName());
        findMember.setPassword(member.getPassword());

        log.debug("회원 정보 수정 성공");
        memberRepository.save(findMember);
    }

    /**
     * 이메일 중복 검사
     *
     * @param email 이메일
     * @return 중복 여부
     */
    public boolean duplicateEmail(String email) {
        List<String> emails = memberRepository.findEmail();
        return !emails.contains(email);
    }

    /**
     * 닉네임 중복 검사
     *
     * @param name 닉네임
     * @return 중복 여부
     */
    public boolean duplicateName(String name) {
        List<String> names = memberRepository.findName();
        return !names.contains(name);
    }

    public Member searchMember(String name) {
        List<Member> find = memberRepository.findByName(name);

        if (find.isEmpty()) {
            log.error("회원이 존재하지 않습니다.");
            throw new IllegalArgumentException("회원이 존재하지 않습니다");
        }

        return find.get(0);
    }

    /**
     * 친구 추가
     *
     * @param member 회원
     * @param friend 친구
     */
    @Transactional
    public void addFriend(Member member, Member friend) {
        Member findMember = findOne(member.getEmail());
        Member findFriend = findOne(friend.getEmail());

        if (findMember == null || findFriend == null) {
            log.error("친구 추가 실패");
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        if (!friendRepository.findFriend(findMember, findFriend).isEmpty()) {
            log.error("친구 추가 실패");
            throw new IllegalArgumentException("이미 친구입니다.");
        }

        Friend newFriend = new Friend(findMember, findFriend);
        Friend contrary = new Friend(findFriend, findMember);

        friendRepository.save(newFriend);
        friendRepository.save(contrary);
        log.debug("친구 추가 성공");
    }

    /**
     * 친구 삭제
     *
     * @param member 회원
     * @param friend 친구
     */
    @Transactional
    public void deleteFriend(Member member, Member friend) {
        Member findMember = findOne(member.getEmail());
        Member findFriend = findOne(friend.getEmail());

        List<Friend> find = friendRepository.findFriend(findMember, findFriend);

        if (find.isEmpty()) {
            log.error("친구 삭제 실패");
            throw new IllegalArgumentException("친구가 아닙니다.");
        }

        List<Friend> findContrary = friendRepository.findFriend(findFriend, findMember);

        Friend find1 = find.get(0);
        Friend find2 = findContrary.get(0);

        friendRepository.delete(find1);
        friendRepository.delete(find2);
        log.debug("친구 삭제 성공");
    }

    /**
     * 친구 이름으로 검색
     *
     * @param name 이름
     * @return 친구
     */
    public List<MemberDTO> searchFriend(String name, Member member) {
        List<Member> find = friendRepository.findFriendByName(name, member);

        if (find.isEmpty()) {
            log.debug("친구가 존재하지 않음");
            throw new IllegalArgumentException("친구가 존재하지 않습니다.");
        }

        List<MemberDTO> memberList = new ArrayList<>();

        for (Member m : find) {
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setName(m.getName());
            memberDTO.setEmail(m.getEmail());
            memberList.add(memberDTO);
        }

        return memberList;
    }

    /**
     * 친구 리스트 조회
     *
     * @param member 회원
     * @return 친구 리스트
     */
    public List<Member> friends(Member member) {
        Member findMember = findOne(member.getEmail());

        List<Member> friends = friendRepository.friends(findMember);
        log.debug("친구 리스트 조회 성공");
        return friends;
    }
}
