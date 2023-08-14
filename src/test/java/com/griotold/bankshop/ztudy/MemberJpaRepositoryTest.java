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
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("기본")
    void basic() throws Exception {
        // given
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when
        Member findMember = memberJpaRepository.findById(member1.getId()).get();

        List<Member> all = memberJpaRepository.findAll();

        List<Member> byUsername = memberJpaRepository.findByUsername("member2");

        // then
        assertThat(findMember).isEqualTo(member1);
        assertThat(all).containsExactly(member1, member2);
        assertThat(byUsername).containsExactly(member2);

    }
    @Test
    @DisplayName("기본 - QueryDSL")
    void basic_QueryDSL_test() throws Exception {
        // given
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when
        Member findMember = memberJpaRepository.findById(member1.getId()).get();

        List<Member> all = memberJpaRepository.findAll_QueryDSL();

        List<Member> byUsername = memberJpaRepository.findByUsername_QueryDSL("member2");

        // then
        assertThat(findMember).isEqualTo(member1);
        assertThat(all).containsExactly(member1, member2);
        assertThat(byUsername).containsExactly(member2);
    }
    @Test
    @DisplayName("Repository 동적 쿼리 - builder 사용 ")
    void searchByBuilder_test() throws Exception {
        // given
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setTeamName("teamB");
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);

        // when
        List<MemberTeamDto> memberTeamDtos = memberJpaRepository.searchByBuilder(condition);

        // then
        assertThat(memberTeamDtos.get(0).getAge()).isEqualTo(40);
//        assertThat(memberTeamDtos.get(0).getUsername()).isEqualTo("member4");
        assertThat(memberTeamDtos).extracting("username").containsExactly("member4");
    }
    @Test
    @DisplayName("builder - teamB만 조회")
    void condition_teamB() throws Exception {
        // given
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setTeamName("teamB");

        // when
        List<MemberTeamDto> memberTeamDtos = memberJpaRepository.searchByBuilder(condition);

        // then
        assertThat(memberTeamDtos)
                .extracting("username")
                .containsExactly("member3", "member4");
    }

    @Test
    @DisplayName("동적 쿼리 - 다중 where 사용")
    void multi_where() throws Exception {
        // given
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setTeamName("teamB");
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);

        // when
        List<MemberTeamDto> memberTeamDtos = memberJpaRepository.search(condition);

        // then
        assertThat(memberTeamDtos)
                .extracting("username")
                .containsExactly("member4");
    }

    @BeforeEach
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