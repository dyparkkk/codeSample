package study.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
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
    public void startJPQL() {
        // member1 찾기
        String qlString = "select m from Member m where m.username =: username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void startQuerydsl() {

        QMember m = new QMember("m");

        Member findMember = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void querydslAnd() {
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"),
                        member.age.goe(10))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void queryFetch() {
        // 리스트로 조회
//        List<Member> fetch = queryFactory
//                .selectFrom(member)
//                .fetch();

        //
        Member member1 = queryFactory
                .selectFrom(member)
                .fetchFirst();

        Long totalCount = queryFactory
                .select(member.count()) // select count(member.id)
                .from(member)
                .fetchOne();

        assertThat(totalCount).isEqualTo(4);
    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 decs
     * 2. 회원 이름 asc
     * 2 없으면 null
     */
    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> fetch = queryFactory
                .selectFrom(member)
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = fetch.get(0);
        Member member6 = fetch.get(1);
        Member memberNull = fetch.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging1() {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(fetch.size()).isEqualTo(2);
    }

    @Test
    public void paging2() {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

    }

    @Test
    public void aggregation(){
        List<Tuple> fetch = queryFactory
                .select(
                        member.count(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.sum()
                )
                .from(member)
                .fetch();

        Tuple tuple = fetch.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
    }

    @Test
    public void group() {
        List<Tuple> fetch = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple team1 = fetch.get(0);
        Tuple team2 = fetch.get(1);

        assertThat(team1.get(team.name)).isEqualTo("teamA");
        assertThat(team1.get(member.age.avg())).isEqualTo(15);
    }

    @Test
    public void join() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    @Test
    public void theta_join(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 예) 회원과 팀을 조회하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: select m, t from Member m left join Team t on t.name =: 'teamA'
     */
    @Test
    public void join_on_filtering() {
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();
        // inner join 할 경우 where절을 사용하는 것과 결과가 같다 !!
    }

    @Test
    public void join_on_no_relation(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for( Tuple tuple : result){
            System.out.println("t= " + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchjoinNoUse() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    @Test
    public void fetchJoinUse() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .join(member.team, team).fetchJoin()
                .where(QMember.member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isTrue();
    }

    // 나이가 가장 많은 회원 조회
    @Test
    public void subQuery() {
        QMember memberSub = new QMember("memberSub");

        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions // static import 가능
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(fetch).extracting("age")
                .containsExactly(40);
    }

    // 나이가 평균 이상인 회원
    @Test
    public void subQueryGeo() {
        QMember memberSub = new QMember("memberSub");

        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(fetch).extracting("age")
                .contains(30, 40);
    }

    // in절
    @Test
    public void subQueryIn() {
        QMember memberSub = new QMember("memberSub");

        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(fetch).extracting("age")
                .containsExactly(20, 30, 40);
    }

    @Test
    public void basicCase() {
        List<Tuple> fetch = queryFactory
                .select(member.username, member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
    }

    @Test
    public void complexCase() {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20")
                        .when(member.age.between(21, 40)).then("21~40")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void constant() {
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result){
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void concat() {

        // {username}_{age}
        List<String> fetch = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        assertThat(fetch.get(0)).isEqualTo("member1_10");
    }

    /**
     * tuple은 꼭 repository 안에서만 사용 !!
     * querydsl에 종속적인 자료구조이므로
     * 서비스나 컨트롤러로 넘길 때는 dto 사용 권장
     */
    @Test
    public void tupleProjection() {
        List<Tuple> fetch = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : fetch) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
        }
    }

    @Test
    public void findByJPQL() {
        List<MemberDto> resultList = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();

    }

    @Test
    public void findDtoBySetter() {
        List<MemberDto> fetch = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
    }

    @Test
    public void findDtoByField() {
        List<MemberDto> fetch = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
    }
    @Test
    public void findDtoByConstructor() {
        List<MemberDto> fetch = queryFactory // 생성자의 타입이 맞아야 함
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
    }

    @Test
    public void findUserDto() {

        QMember memberSub = new QMember("memberSub");

        List<UserDto> fetch = queryFactory // 생성자의 타입이 맞아야 함
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub), "age")
                ))
                .from(member)
                .fetch();
    }

    @Test
    public void findDtoByQueryProjection() {
        List<MemberDto> fetch = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();
    }
}
