package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.model.entity.Advise;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.AdviseService;
import com.jingdong.manager.service.impl.AdviseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "adviseController", value = "/sys/advise/*")

public class AdviseController extends HttpServlet {

    private AdviseService adviseService = new AdviseServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requestURI = req.getRequestURI();
        String substring = requestURI.substring(requestURI.indexOf("/sys/advise") + "/sys/advise".length());
        if (substring.equals("")) {
            this.showAdvise(req, resp);
        } else if (substring.equals("/list")) {
            ApiRestResponse apiRestResponse = this.adviseList(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else {
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
        }
    }

        public void showAdvise(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/WEB-INF/advise.jsp").forward(req, resp);

        }

        public ApiRestResponse adviseList(HttpServletRequest req, HttpServletResponse resp) {
            List<Advise> adviseList = new ArrayList<>();
            try {
                adviseList = adviseService.selectAll();
            } catch (BusinessException e) {
                return ApiRestResponse.error(e.getCode(), e.getMsg());
            } catch (Exception e) {
                return ApiRestResponse.error(e.getMessage());
            }
            return ApiRestResponse.success(adviseList);
        }


    }
