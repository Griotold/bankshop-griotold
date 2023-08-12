package com.griotold.bankshop.ztudy;

import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.List;

import static com.griotold.bankshop.ztudy.QMember.*;
import static com.griotold.bankshop.ztudy.QTeam.*;
import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.*;
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    private EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }
    @Test
    @DisplayName("JPQL로 먼저")
    void start_JPQL() {
        // given
        String jpql = "select m from Member m where m.username = :username";
        Member findMember = em.createQuery(jpql, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        // when
        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
    @Test
    @DisplayName("queryDsl 시작")
    void start_queryDsl() {
        // given

        // when
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName("where절")
    void where_test() {
        // when
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
    @Test
    @DisplayName("member로 시작하고 between : 30~40세")
    void startWith_between() {

        // when
        List<Member> members = queryFactory
                .selectFrom(member)
                .where(member.username.startsWith("member")
                        .and(member.age.between(30, 40)))
                .fetch();
        // then
        assertThat(members.size()).isEqualTo(2);
        assertThat(members.get(0).getAge()).isEqualTo(30);
        assertThat(members.get(0).getUsername()).isEqualTo("member3");
    }
    @Test
    @DisplayName("and는 생략 가능")
    void and_생략() {
        // when
        List<Member> members = queryFactory
                .selectFrom(member)
                .where(member.username.like("%member%"),
                        member.age.in(20, 40))
                .fetch();
        // then
        assertThat(members.size()).isEqualTo(2);
        assertThat(members.get(0).getUsername()).isEqualTo("member2");
        assertThat(members.get(1).getUsername()).isEqualTo("member4");
    }
    @Test
    @DisplayName("fetch, fetchFirst - 결과값 가져오기")
    void fetch_test() {
        // when
        List<Member> members = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst(); // limit(1).fetchOne();
        // then
        assertThat(members.size()).isEqualTo(4);
        assertThat(fetchFirst.getAge()).isEqualTo(10);

    }
    @Test
    @DisplayName("fetchOne은 값이 여러개라면 예외발생")
    void fetchOne_exception() throws Exception {
        // then
        assertThatThrownBy(() -> queryFactory.selectFrom(member)
                .fetchOne()).isInstanceOf(NonUniqueResultException.class);
    }
    @Test
    @DisplayName("count - 페이징 관련")
    void fetchResults_test() {
        // when
        Long count = queryFactory.select(member.count())
                .from(member)
                .fetchOne();
        // then
        assertThat(count).isEqualTo(4);
    }
    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순
     * 2. 회원 이름 올림차순
     * 단 2에서 회원 이름이 없으면 마지막에 출력 (nulls last)
     *
     * */
    @Test
    @DisplayName("정렬")
    void sort_test() {
        // given
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        // when
        List<Member> members = queryFactory.selectFrom(member)

                .orderBy(member.age.desc())
                .orderBy(member.username.asc().nullsLast())
                .fetch();
        int size = members.size();
        Member member5 = members.get(0);
        Member member6 = members.get(1);
        Member memberNull = members.get(2);

        // then
        assertThat(size).isEqualTo(7);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();

    }

    @Test
    @DisplayName("offset, limit - 페이징")
    void paging1() {
        // when
        List<Member> result = queryFactory.selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) // 1개를 스킵
                .limit(2) // 2개를 가져옴
                .fetch();

        Member member3 = result.get(0);
        Member member2 = result.get(1);

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(member3.getUsername()).isEqualTo("member3");
        assertThat(member2.getUsername()).isEqualTo("member2");
    }
    @Test
    @DisplayName("집합")
    void aggregation_test() {
        // when
        List<Tuple> result = queryFactory.select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                        )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);

        // then
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }
    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라
     *
     */
    @Test
    @DisplayName("그룹 바이")
    void group() throws Exception {
        // when
        List<Tuple> result = queryFactory.select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        // then
        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    @Test
    @DisplayName("조인 첫번째")
    void join() throws Exception {
        // when
        List<Member> result = queryFactory.selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        // then
        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }
    @Test
    @DisplayName("세타 조인")
    void theta_join() throws Exception {
        // given
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        // when
        List<Member> result = queryFactory.select(member)
                .from(member, team) // from 절에 그냥 조인하고 싶은 테이블을 나열
                .where(member.username.eq(team.name))
                .fetch();

        // then
        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }
    /**
     * JPQL : select m, t from Member m left join m.team t on t.name = 'teamA'
     * */
    @Test
    @DisplayName("회원은 left 조인, teamA인 팀만 조인")
    void join_in_filtering() throws Exception {
        // when
        List<Tuple> result = queryFactory.select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
        // then
    }
    /**
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     * */
    @Test
    @DisplayName("연관관계가 없는 외부조인")
    void join_on_no_relation() throws Exception {
        // given
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        // when
        List<Tuple> result = queryFactory.select(member, team)
                .from(member)
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
        // then
    }
    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    @DisplayName("페치 조인 미적용")
    void fetch_join_no() throws Exception {
        // given
        em.flush();
        em.clear();

        // when
        Member findMember = queryFactory.selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean isLoaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        // then
        assertThat(isLoaded).as("페치 조인 미적용").isFalse();
    }
    @Test
    @DisplayName("페치 조인 적용")
    void fetch_join_on() throws Exception {
        // given
        em.flush();
        em.clear();

        // when
        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean isLoaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        // then
        assertThat(isLoaded).as("페치 조인 미적용").isTrue();
    }
    /**
     * 나이가 가장 많은 회원 조회
     * */
    @Test
    @DisplayName("서브 쿼리")
    void subQuery_test() throws Exception {
        // given
        QMember memberSub = new QMember("memberSub");
        // when
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        // then
        assertThat(result).extracting("age")
                .containsExactly(40);
    }
    /**
     * 나이가 평균 이상 회원 조회
     * */
    @Test
    @DisplayName("서브 쿼리 - 나이가 평균 이상")
    void subQuery_avg() throws Exception {
        // given
        QMember memberSub = new QMember("memberSub");
        // when
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        // then
        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }

    @Test
    @DisplayName("서브 쿼리 - in 활용")
    void subQuery_in() throws Exception {
        // given
        QMember memberSub = new QMember("memberSub");
        // when
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        // then
        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }
    @Test
    @DisplayName("서브쿼리 - 셀렉트 절에")
    void subQuery_select() throws Exception {
        // given
        QMember memberSub = new QMember("memberSub");

        // when
        List<Tuple> result = queryFactory
                .select(member.username,
                        select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
        // then
    }

}
