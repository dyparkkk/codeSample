package com.example.helloJpa;

import com.example.helloJpa.domain.Member;

import javax.persistence.*;
import java.util.List;

public class Run {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Member member = new Member();
            member.setName("member1");
            em.persist(member);

            List<Member> memberList = em.createQuery("select m from Member m", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

        } catch (Exception e){

        }
    }
}
