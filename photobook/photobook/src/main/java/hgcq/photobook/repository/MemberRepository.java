package hgcq.photobook.repository;

import hgcq.photobook.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext
    private final EntityManager em;

    /**
     * 멤버 저장
     *
     * @param member 저장할 멤버
     * @return 멤버의 아이디
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
     * 사용자 하나 검색
     *
     * @param email 사용자 이메일
     * @return 사용자
     */
    public Member findOne(String email) {
        return em.createQuery("select m from Member m where email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    /**
     * 사용자 전체 검색
     *
     * @return 사용자 리스트
     */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /**
     * 사용자 이메일 검색
     *
     * @return 사용자 이메일 리스트
     */
    public List<String> findEmail() {
        return em.createQuery("select m.email from Member m", String.class)
                .getResultList();
    }
}
