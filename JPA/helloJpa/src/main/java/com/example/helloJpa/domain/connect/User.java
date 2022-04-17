package com.example.helloJpa.domain.connect;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class User {

    @Id
    private String id;

    @Column(name = "name", insertable = true, updatable = true)
    private String name;

    @ManyToOne  //(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false, unique = true) //not null , unique -> 이름 이상함
    private String userId;                       // @Table(에서 많이 줌

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTime;





}
