package com.example.mvc1_servlet.web.frontcontroller.v5.adapter;

import com.example.mvc1_servlet.web.frontcontroller.ModelView;
import com.example.mvc1_servlet.web.frontcontroller.MyView;
import com.example.mvc1_servlet.web.frontcontroller.v4.ControllerV4;
import com.example.mvc1_servlet.web.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws ServletException, IOException {
        ControllerV4 controller = (ControllerV4) handler;

        Map<String, String> paramMap = createParamMap(req);
        Map<String, Object> model = new HashMap<>();
        String viewPath = controller.process(paramMap, model);

        ModelView modelView = new ModelView(viewPath);
        modelView.setModel(model);
        return modelView;
    }

    private Map<String, String> createParamMap(HttpServletRequest req) {
        Map<String, String> paramMap = new HashMap<>();
        req.getParameterNames().asIterator()
                .forEachRemaining(param -> paramMap.put(param, req.getParameter(param)));
        return paramMap;
    }

}
