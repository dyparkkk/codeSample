package com.example.dddstart.order.domain;

import com.example.dddstart.member.MemberId;

import javax.persistence.*;

@Embeddable
public class Orderer {

    // MemberId에 정의된 칼럼 이름을 변경하기 위해
    // @AttributeOverride 애노테이션 사용
    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "orderer_id"))
    )
    private MemberId memberId;

    @Column(name = "orderer_name")
    private String name;
}
