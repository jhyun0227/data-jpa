package study.datajpa.team.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.member.entity.Member;
import study.datajpa.team.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamJpaRepository {

    private final EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public List<Team> findAll() {
        String jpql = "select t from Team t";

        return em.createQuery(jpql, Team.class)
                .getResultList();
    }

    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    public long count() {
        String jpql = "select count(t) from Team t";

        return em.createQuery(jpql, Long.class)
                .getSingleResult();
    }
}
