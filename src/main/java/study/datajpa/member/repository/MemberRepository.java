package study.datajpa.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.member.dto.MemberDto;
import study.datajpa.member.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.member.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username); //Optional 단건

    /**
     * 페이징 쿼리
     * Page의 경우 count 쿼리를 따로 분리할 수 있다.
     */
    @Query(
            value = "select m from Member m left join m.team t"
            , countQuery = "select count(m) from Member m"
    )
    Page<Member> findPageByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    /**
     * 벌크 수정 쿼리
     * ##### 주의 #####
     * 1. 벌크성 업데이트는 영속성 컨텍스트를 무시하고 DB에 바로 쿼리를 꽃아 넣는다.
     * 2. 그렇기 때문에 같은 트랜잭션 안에서 영속성 컨텍스트에서 관리되는 값과 DB의 값이 다를 수 있다.
     * 3. 그래서 벌크 연산시 영속성 컨텍스트를 다 날려야 한다.
     *
     * 권장방안
     * 1. 영속성 컨텍스트에 엔티티가 없는 상태에서 벌크 연산을 먼저 실행한다.
     * 2. 부득이하게 영속성 컨텍스트에 엔티티가 있으면 벌크 연산 직후 영속성 컨텍스트를 초기화 한다.
     */
    @Modifying(clearAutomatically = true) //JPA executeUpaate의 역할을 함으로 꼭 넣어야 한다.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /**
     * fetch join의 다양한 방법
     */
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
//    @EntityGraph("Member.all") //Member 클래스에 정의한 NamedEntityGraph.. 잘 사용하진 않는 것 같다.
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    /**
     * Hint & Lock
     * readOnly라는 힌트를 줌으로서 스냅샷을 만들지 않아 성능의 최적화를 한다.
     * 자주 쓰일일은 없을 것..... 테스트를 통해 적용할지 말지 정해야 한다.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true") )
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
