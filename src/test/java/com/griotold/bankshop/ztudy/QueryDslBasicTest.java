package com.griotold.bankshop.ztudy;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

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
        QMember m = new QMember("m");

        // when
        Member findMember = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetchOne();

        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
