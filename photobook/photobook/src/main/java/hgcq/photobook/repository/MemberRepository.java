package hgcq.photobook.repository;

import hgcq.photobook.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 회원 가입
 * 회원 조회
 * 이메일 리스트 조회
 */

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext
    private final EntityManager em;

    /**
     * 회원 저장
     *
     * @param member 저장할 회원
     * @return 회원 아이디
     */
    public Long save(Member member) {
        if (member.getId() == null) {
            em.persist(member);
        } else {
            em.merge(member);
        }
        return member.getId();
    }


    /**
     * 회원 검색
     *
     * @param email 회원 이메일
     * @return 회원
     */
    public Member findOne(String email) {
        return em.createQuery("select m from Member m where email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    /**
     * 회원 이메일 리스트 검색
     *
     * @return 회원 이메일 리스트
     */
    public List<String> findEmail() {
        return em.createQuery("select m.email from Member m", String.class)
                .getResultList();
    }

    /**
     * 회원 이름 리스트 검색
     *
     * @return 회원 이름 리스트
     */
    public List<String> findName() {
        return em.createQuery("select m.name from Member m", String.class)
                .getResultList();
    }
}
