package com.example.shiyan1.controller;

import com.alibaba.fastjson.JSON;
import com.example.shiyan1.common.Constant;
import com.example.shiyan1.entity.User;

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

@WebServlet(name = "UserCreateServlet", value = "/user/create")
public class UserCreateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String)request.getParameter("username");
        String password = (String) request.getParameter("password");
        Map<String, Object> res = new HashMap<String, Object>();
        if (username == null || password == null) {
            throw new RuntimeException("系统异常");
        }
        Boolean flag = false;
        for (User user : Constant.userList) {
            if (user.getUserName().equals(username)) {
                res.put("code", 600);
                res.put("msg", "用户名已存在");
                res.put("data", "");
                flag = true;
                break;
            }
        }
        if(!flag) {
            User user = new User();
            user.setUserId((long) (Constant.userList.size() + 1));
            user.setUserName(username);
            user.setPassword(password);
            Constant.userList.add(user);
            res.put("code", 200);
            res.put("msg", "注册成功");
            res.put("data", user);
        }
        String json = JSON.toJSONString(res);
        response.getWriter().println(json);
    }
}
