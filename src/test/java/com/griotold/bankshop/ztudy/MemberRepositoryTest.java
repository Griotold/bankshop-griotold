package com.griotold.bankshop.ztudy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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
    /**
     * 스프링 데이터 JPA 페이징과 정렬
     * */
    @Test
    @DisplayName("Page와 Pageable")
    void page_pageable() throws Exception {
        // given
        Member member5 = new Member("member5", 10);
        Member member6 = new Member("member6", 10);
        Member member7 = new Member("member7", 10);
        Member member8 = new Member("member8", 10);
        em.persist(member5);
        em.persist(member6);
        em.persist(member7);
        em.persist(member8);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // then
        int totalPages = page.getTotalPages(); // 총 페이지수
        List<Member> content = page.getContent(); // 가져온 레코드들
        long totalCounts = page.getTotalElements(); // 총 데이터수
        int pageNumber = page.getNumber(); // 현재 페이수 수 -> 첫 페이지 : 0
        boolean first = page.isFirst(); // 첫 페이지냐
        boolean last = page.isLast(); // 마지막 페이지냐
        boolean hasNext = page.hasNext(); // 다음 페이지가 있냐

        System.out.println("pageNumber = " + pageNumber);
        System.out.println("totalPages = " + totalPages);
        System.out.println("totalCounts = " + totalCounts);
        content.stream().forEach(System.out::println);
    }

    /**
     * Slice : 총 페이지수와 총 데이터수는 모른다
     * limit 값 + 1 만큼 조회한다.
     * */
    @Test
    @DisplayName("slice")
    void slice() throws Exception {
        // given
        Member member5 = new Member("member5", 10);
        Member member6 = new Member("member6", 10);
        Member member7 = new Member("member7", 10);
        Member member8 = new Member("member8", 10);
        em.persist(member5);
        em.persist(member6);
        em.persist(member7);
        em.persist(member8);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        // then
        // int totalPages = page.getTotalPages(); // 총 페이지수
        // long totalCounts = page.getTotalElements(); // 총 데이터수
        List<Member> content = page.getContent(); // 가져온 레코드들
        int pageNumber = page.getNumber(); // 현재 페이수 수 -> 첫 페이지 : 0
        boolean first = page.isFirst(); // 첫 페이지냐
        boolean last = page.isLast(); // 마지막 페이지냐
        boolean hasNext = page.hasNext(); // 다음 페이지가 있냐

        System.out.println("pageNumber = " + pageNumber);
        content.stream().forEach(System.out::println);
        // System.out.println("totalPages = " + totalPages);
        // System.out.println("totalCounts = " + totalCounts);
    }
    /**
     * Page<Member>도 엔티티라 바로 리턴하지말고
     * DTO로 변환해서 리턴!
     * */
    @Test
    @DisplayName("page객체를 DTO로")
    void page_to_dto() throws Exception {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member5 = new Member("member5", 10, teamA);
        Member member6 = new Member("member6", 10, teamA);
        Member member7 = new Member("member7", 10, teamB);
        Member member8 = new Member("member8", 10, teamB);
        em.persist(member5);
        em.persist(member6);
        em.persist(member7);
        em.persist(member8);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getUsername(), member.getAge()));
        toMap.stream().forEach(System.out::println);

        // then
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