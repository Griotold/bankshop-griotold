package com.griotold.bankshop.ztudy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("기본")
    void basic() throws Exception {
        // given
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        Member findMember = memberRepository.findById(member1.getId()).get();

        List<Member> all = memberRepository.findAll();

        List<Member> byUsername = memberRepository.findByUsername("member2");

        // then
        assertThat(findMember).isEqualTo(member1);
        assertThat(all).containsExactly(member1, member2);
        assertThat(byUsername).containsExactly(member2);

    }
    @Test
    @DisplayName("스프링 데이터 JPA에서 QueryDSL 사용")
    void 스프링_데이터_JPA_plus_QueryDSL() throws Exception {
        // given
        setUp();
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setTeamName("teamB");
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);

        // when
        List<MemberTeamDto> memberTeamDtos = memberRepository.search(condition);

        // then
        assertThat(memberTeamDtos)
                .extracting("username")
                .containsExactly("member4");
    }

//    @BeforeEach
    void setUp() {
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
}