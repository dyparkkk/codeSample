package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        Member member = new Member("memberA", 10, teamA);
        Member member1 = new Member("memberB", 20, teamA);
        Member member2 = new Member("memberC", 30, teamB);
        Member member3 = new Member("memberD", 40, teamB);

        em.persist(member);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        em.flush();
        em.clear();

        List<Member> memberList = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member findMember : memberList) {
            System.out.println("findMember = " + findMember);
            System.out.println("-> findMember.getTeam = " + findMember.getTeam());
        }
    }

    @Test
    void JpaEventBaseEntity() throws InterruptedException {
        // given
        Member member = new Member("member1");
        memberRepository.save(member); // @Prepersist

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); //Preupdate
        em.clear();

        System.out.println(member);
    }

}