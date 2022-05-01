package hello.login.domain.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Member {

    private Long id;

    @NotNull
    private String loginId;
    @NotNull
    private String name;
    @NotNull
    private String password;
}
