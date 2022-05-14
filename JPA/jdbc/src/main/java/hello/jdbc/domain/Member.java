package hello.jdbc.domain;

import lombok.Data;

/**
 * @Data : equals랑 hashcode 만들어줌 , toString도 예쁘게 만들어줌
 */
@Data
public class Member {
    private String memberId;
    private int money;

    public Member() {
    }

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }
}
