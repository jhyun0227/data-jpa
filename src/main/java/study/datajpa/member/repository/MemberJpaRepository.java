package study.datajpa.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.member.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        String jpql = "select m from Member m";

        return em.createQuery(jpql, Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public long count() {
        String jpql = "select count(m) from Member m";

        return em.createQuery(jpql, Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        String jpql = "select m from Member m where m.username = :username and m.age > :age";

        return em.createQuery(jpql, Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByPage(int age, int offset, int limit) {
        String jpql = "select m from Member m where m.age = :age order by m.username desc";

        return em.createQuery(jpql, Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age) {
        String jpql = "select count(m) from Member m where m.age = :age";

        return em.createQuery(jpql, Long.class)
                .setParameter("age", age)
                .getSingleResult();

    }

    /**
     * 벌크 수정 쿼리
     * ##### 주의 #####
     * 1. 벌크성 업데이트는 영속성 컨텍스트를 무시하고 DB에 바로 쿼리를 꽃아 넣는다.
     * 2. 그렇기 때문에 같은 트랜잭션 안에서 영속성 컨텍스트에서 관리되는 값과 DB의 값이 다를 수 있다.
     * 3. 그래서 벌크 연산시 영속성 컨텍스트를 다 날려야 한다.
     */
    public int bulkAgePlus(int age) {
        String jpql = "update Member m set m.age = m.age +1 where m.age >= :age";

        int resultCount = em.createQuery(jpql)
                .setParameter("age", age)
                .executeUpdate();

        return resultCount;
    }
}
