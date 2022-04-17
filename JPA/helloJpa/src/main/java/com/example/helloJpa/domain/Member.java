package com.example.helloJpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    // 좋은 설계가 아님
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
