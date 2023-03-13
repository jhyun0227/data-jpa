package study.datajpa.member.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.member.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        String jpql = "select m from Member m";

        return em.createQuery(jpql, Member.class)
                .getResultList();
    }
}
