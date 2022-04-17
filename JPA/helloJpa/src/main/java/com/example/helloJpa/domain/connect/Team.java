package com.example.helloJpa.domain.connect;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<User> users = new ArrayList<>();

    // == 연관관계 편의 메서드 ==//
    public void changeMember(User user){
        user.setTeam(this);
        users.add(user);
    }
}
