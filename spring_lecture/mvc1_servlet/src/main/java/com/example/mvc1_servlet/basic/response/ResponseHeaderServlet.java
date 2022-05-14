package com.example.mvc1_servlet.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //[status-line]
        resp.setStatus(HttpServletResponse.SC_OK);

        //[response-header]
        resp.setHeader("Content-Type", "text/plain;charset=utf-8");
        resp.setHeader("my-header", "hello");

        // 헤더 편의 메서드
//        resp.setContentType("text/plain");
//        resp.setCharacterEncoding("utf-8");

        // 쿠키 설정
        cookie(resp);

        // 리다이렉트
        resp.sendRedirect("/basic/hello-form.html");

        resp.getWriter().write("ok");
    }

    private void cookie(HttpServletResponse resp) {
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600);
        resp.addCookie(cookie);
    }
}
