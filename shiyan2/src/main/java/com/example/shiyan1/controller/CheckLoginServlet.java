package com.example.shiyan1.controller;

import com.alibaba.fastjson.JSON;
import com.example.shiyan1.entity.User;
import com.example.shiyan1.service.UserService;
import com.example.shiyan1.service.impl.UserServiceImpl;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author word
 */
@WebServlet(name = "CheckLoginServlet", value = "/user/check_login")
public class CheckLoginServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        Map<String, Object> map = new HashMap<>();

        try {
            User user = userService.checkLogin(userName, password);
            HttpSession session = request.getSession();
            session.setAttribute("currUser", user);
            map.put("code", 200);
            map.put("msg", "SUCCESS");
            map.put("data", "");
        } catch (RuntimeException e) {
            map.put("code", 500);
            map.put("msg", e.getMessage());
        }
        String json = JSON.toJSONString(map);
        response.getWriter().println(json);
    }


}
