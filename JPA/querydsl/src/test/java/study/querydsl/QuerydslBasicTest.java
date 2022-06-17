package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    // 필드로 빼서 사용하는 것을 권장 ( 동시성 문제가 없음 )
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

        Member member1 = queryFactory
                .selectFrom(member)
                .fetchFirst();

        Long totalCount = queryFactory
                .select(member.count()) // select count(member.id)
                .from(member)
                .fetchOne();

        assertThat(totalCount).isEqualTo(4);

        // 이제는 사용 x
//        QueryResults<Member> memberQueryResults = queryFactory
//                .selectFrom(member)
//                .fetchResults();
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

        // on절은 조인할 때 필터링, where은 조인 결과에서 필터링
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
        List<MemberDto> resultList =
                em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) " +
                                "from Member m", MemberDto.class)
                .getResultList();

    }

    /**
     * querydsl은 3가지의 dto로 조회하는 방법을 제공해줌
     */
    @Test
    public void findDtoBySetter() {
        // setter를 사용하는 방법
        List<MemberDto> fetch = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
    }

    @Test
    public void findDtoByField() {
        // 필드 주입
        List<MemberDto> fetch = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
    }
    @Test
    public void findDtoByConstructor() {
        // 생성자 사용 방법 -> 생성자의 매개변수 타입이 맞아야 함
        List<MemberDto> fetch = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
    }

    @Test
    public void findUserDto() {
        // dto와 entity의 field의 이름이 다른 경우
        QMember memberSub = new QMember("memberSub");

        List<UserDto> fetch = queryFactory
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
        /**
         * @QueryProjection 방식 (생성자로 dto를 만드는 방식)
         * 런타임 오류 대신 컴파일 오류를 내줌 -> 안전
         * 단점 :
         *  1. Q-file을 생성해줘야함
         *  2. dto가 querydsl에 의존적으로 설계됨
         */
        List<MemberDto> fetch = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();
    }

    @Test
    void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        searchMember1(usernameParam, ageParam);
    }

    // BooleanBuilder - 동적쿼리 생성
    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        BooleanBuilder builder = new BooleanBuilder();
        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }

        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
        return result;
    }

    @Test
    void dynamicQuery_WhereParam() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> members = searchMember2(usernameParam, ageParam);
        assertThat(members.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        /**
         * - where절에 null조건은 무시된다
         * - 메서드를 다른 쿼리에서도 재사용할 수 있다
         * - 쿼리 자체의 가독성이 높아진다.
         */
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCond), ageEq(ageCond))
                .fetch();
        return result;
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private Predicate ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    @Test
    void bulkUpdate() {
        /**
         * bulk연산은 영속성 컨텍스트와 관계없이 DB에 바로 쿼리를 날림
         *  -> 영속성 컨텍스트와 DB의 상태가 달라짐
         *   이후 selcet해도 repeatableRead라서 바뀐게 적용이 안됨
         */
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.loe(20))
                .execute();

        em.flush();
        em.clear();
    }

    @Test
    void bulkAdd() {
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();
    }

    @Test
    void bulkDelete() {
        // 21살 이상 회원 모두 삭제
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(21))
                .execute();
    }

    /**
     * sql함수 호출해서 사용하기
     */
    @Test
    void sqlFunction() {
        List<String> result = queryFactory
                .select(Expressions.stringTemplate(
                        "function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"))
                .from(member)
                .fetch();
    }

    @Test
    void sqlFunction2() {
        queryFactory
                .select(member.username)
                .from(member)
                .where(member.username.eq(member.username.lower()))
//                .where(member.username.eq(
//                        Expressions.stringTemplate("function('lower', {0})", member.username)))
                .fetch();
    }


}
