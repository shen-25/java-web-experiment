package com.jingdong.manager.controller;

import com.jingdong.manager.common.Constant;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.ProductService;
import com.jingdong.manager.service.RoleUserService;
import com.jingdong.manager.service.TransactionService;
import com.jingdong.manager.service.impl.ProductServiceImpl;
import com.jingdong.manager.service.impl.RoleUserServiceImpl;
import com.jingdong.manager.service.impl.TransactionServiceImpl;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "homeController", value = "/home/*")
public class HomeController extends HttpServlet{

    private RoleUserService roleUserService = new RoleUserServiceImpl();
    private ProductService productService = new ProductServiceImpl();
    private TransactionService transactionService = new TransactionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      this.home(req, resp);
    }


    public void home(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requestURI = req.getRequestURI();
        String substring = requestURI.substring(requestURI.indexOf("/jd/home") + "/jd/home".length());
        if(substring.equals("")) {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute(Constant.CURRENT_USER);
            session.setAttribute("user", user);
            Set<UserMenuTreeVo> userMenuTreeVos = roleUserService.menuList(user.getUserId());
            session.setAttribute("node_list", userMenuTreeVos);
            req.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(req, resp);
        } else if (substring.equals("/page")) {
            this.homePage(req, resp);
        } else{
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
        }
    }

    public void homePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long upState = productService.selectByUpState();
        Long downState = productService.selectByDownState();
        Long allProduct = productService.selectAllProduct();
        Long tightProduct = productService.selectByTightProduct();
        req.setAttribute("upState", upState);
        req.setAttribute("downState", downState);
        req.setAttribute("allProduct", allProduct);
        req.setAttribute("tightProduct", tightProduct);


        Long todayOrderValues = transactionService.todayOrderValues();
        Long todayOrderTotal  = transactionService.todayOrderTotal();
        Long yesterdayOrderValues = transactionService.yesterdayOrderValues();
        req.setAttribute("todayOrderTotal", todayOrderTotal);
        req.setAttribute("todayOrderValues", todayOrderValues);
        req.setAttribute("yesterdayOrderValues", yesterdayOrderValues);
        req.getRequestDispatcher("/WEB-INF/jsp/homePage.jsp").forward(req, resp);
    }

}
