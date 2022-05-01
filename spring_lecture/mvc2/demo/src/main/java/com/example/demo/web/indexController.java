package com.example.demo.web;

import com.example.demo.domain.Member;
import com.example.demo.web.argumentResolver.Login;
import org.springframework.stereotype.Controller;

@Controller
public class indexController {

    private String index(@Login Member member) {

        return "/";
    }
}
