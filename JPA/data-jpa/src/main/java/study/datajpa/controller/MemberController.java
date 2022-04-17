package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("members")
    public Page<MemberDto> pageMembers(@PageableDefault(size = 5) Pageable pageable) {
        Page<MemberDto> map = memberRepository
                .findAll(pageable).map(MemberDto::new);
        return map;
    }

    @PostConstruct
    public void init() {
        for (int i =0; i< 100; i++){
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
