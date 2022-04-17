package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private int age;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public MemberDto(Member member) {
        username = member.getUsername();
        age = member.getAge();
    }
}
