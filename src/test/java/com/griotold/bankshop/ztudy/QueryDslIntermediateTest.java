package com.griotold.bankshop.ztudy;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import java.util.List;

import static com.griotold.bankshop.ztudy.QMember.member;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class QueryDslIntermediateTest {

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
    @DisplayName("프로젝션 기본")
    void simple_projection() throws Exception {
        // when
        List<String> result = queryFactory.select(member.username)
                .from(member)
                .fetch();

        for (String username : result) {
            System.out.println("username = " + username);
        }
    }
    @Test
    @DisplayName("프로젝션 - 튜플")
    void tuple_projection() throws Exception {
        // when
        List<Tuple> result = queryFactory.select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);

            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }
    @Test
    @DisplayName("JPQL로 DTO 프로젝션")
    void findDtoByJPQL() throws Exception {
        // given
        String jpql = "select new com.griotold.bankshop.ztudy.MemberDto(m.username, m.age) from Member m";

        // when
        List<MemberDto> resultList = em.createQuery(jpql, MemberDto.class).getResultList();

        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto = " + memberDto);
        }
    }
    @Test
    @DisplayName("QueryDSL DTO 프로젝션 1 - 세터")
    void findDtoBySetter() throws Exception {
        // when
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
//        for (MemberDto memberDto : result) {
//            System.out.println("memberDto = " + memberDto);
//        }
        result.stream().forEach(System.out::println);
    }
    @Test
    @DisplayName("QueryDSL DTO 프로젝션 2 - 필드")
    void findDtoByField() throws Exception {
        // when
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
        result.stream().forEach(System.out::println);
    }
    @Test
    @DisplayName("QueryDSL DTO 프로젝션 3 - 생성자")
    void findDtoByConstructor() throws Exception {
        // when
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        result.stream().forEach(System.out::println);
    }

    @Test
    @DisplayName("Member2Dto 프로젝션")
    void findMember2DtoByField() throws Exception {
        // when
        List<Member2Dto> result = queryFactory
                .select(Projections.fields(Member2Dto.class,
                        member.username.as("name"), // 필드 이름 as로 맞춰주기
                        member.age))
                .from(member)
                .fetch();
        result.stream().forEach(System.out::println);
    }
    @Test
    @DisplayName("ExpressionUtils - 서브 쿼리 활용")
    void ExpressionUtils_test() throws Exception {
        // given
        QMember memberSub = new QMember("memberSub");

        // when
        List<Member2Dto> result = queryFactory
                .select(Projections.fields(Member2Dto.class,
                        member.username.as("name"),

                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub), "age")
                ))
                .from(member)
                .fetch();

        result.stream().forEach(System.out::println);
    }
    @Test
    @DisplayName("@QueryProjection")
    void 골뱅이_QueryProjection() throws Exception {
        // when
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        result.stream().forEach(System.out::println);
    }
    @Test
    @DisplayName("동적 쿼리 1 - BooleanBuilder")
    void dynamic_query_BooleanBuilder() throws Exception {
        // given
        String usernameParam = "member1";
        Integer ageParam = null;

        // when
        List<Member> result = searchMember1(usernameParam, ageParam);

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        // BooleanBuilder에 초기값 세팅도 가능
        // new BooleanBuilder(member.username.eq(usernameCond))
        BooleanBuilder builder = new BooleanBuilder();
        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }

        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory.selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Test
    @DisplayName("동적 쿼리 2 - 다중 where")
    void dynamic_query_multiple_where() throws Exception {
        // given
        String usernameParam = "member1";
        Integer ageParam = 10;

        // when
        List<Member> result = searchMember2(usernameParam, ageParam);

        // then
        assertThat(result.size()).isEqualTo(1);

    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                .where(allEq(usernameCond, ageCond)) // null이 들어가면 무시!
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {
//        if (usernameCond == null) {
//            return null;
//        }
//        return member.username.eq(usernameCond);
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }
}
