package com.example.shiyan1.controller;

import com.alibaba.fastjson.JSON;
import com.example.shiyan1.common.Constant;
import com.example.shiyan1.entity.User;
import com.example.shiyan1.service.UserService;
import com.example.shiyan1.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CheckRegisterServlet", value = "/user/create")
public class CheckRegisterServlet extends HttpServlet {
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String)request.getParameter("username");
        String password = (String) request.getParameter("password");
        Map<String, Object> res = new HashMap<>();
        try {
            userService.create(username, password);
            res.put("code", 200);
            res.put("msg", "SUCCESS");
            res.put("data", "");
        } catch (RuntimeException e) {
            res.put("code", 600);
            res.put("msg", e.getMessage());
            res.put("data", "");
        }
        String json = JSON.toJSONString(res);
        response.getWriter().println(json);
    }
}
