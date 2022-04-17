package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result =
                memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findMemberDto() {
        Team t1 = new Team("team1");
        teamRepository.save(t1);

        Member m1 = new Member("AAA", 10);
        m1.changTeam(t1);
        memberRepository.save(m1);

        List<MemberDto> memberDtoList = memberRepository.findMemberDto();
        for (MemberDto dto :
                memberDtoList) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);


        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member:
                byNames) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void page() {
        memberRepository.save(new Member("AAA1", 10));
        memberRepository.save(new Member("AAA2", 10));
        memberRepository.save(new Member("AAA3", 10));
        memberRepository.save(new Member("AAA4", 10));
        memberRepository.save(new Member("AAA5", 10));

        int age = 10;
        PageRequest pr = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pr);
//        Slice<Member> page = memberRepository.findSliceByAge(age, pr);

        // page Dto로 반환
        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getUsername(), m.getAge()));

        List<Member> content = page.getContent();
//        List<Member> content1 = slice.getContent();
//        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void findMemberLazy(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("AAA1", 10));
        memberRepository.save(new Member("AAA2", 10));

        em.flush();
        em.clear();

        // N+1
        List<Member> all = memberRepository.findAll();
    }

    @Test
    public void queryHint() {
        Member member = new Member("AAA1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername(member.getUsername());
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        memberRepository.findLockByUsername(member.getUsername());

    }
}