package com.example.mvc1_servlet.web.frontcontroller.v5.adapter;

import com.example.mvc1_servlet.web.frontcontroller.ModelView;
import com.example.mvc1_servlet.web.frontcontroller.v3.ControllerV3;
import com.example.mvc1_servlet.web.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ControllerV3HandlerAdapter implements MyHandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV3);
    }

    @Override
    public ModelView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws ServletException, IOException {



    }
}
