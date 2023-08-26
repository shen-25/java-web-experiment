package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.model.entity.Notice;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.NoticeService;
import com.jingdong.manager.service.UserService;
import com.jingdong.manager.service.impl.NoticeServiceImpl;
import com.jingdong.manager.service.impl.UserServiceImpl;
import com.jingdong.manager.utils.MyIOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "noticeController", value = "/notice/*")
public class NoticeController extends HttpServlet {

    private NoticeService noticeService = new NoticeServiceImpl();
    private UserService userService = new UserServiceImpl();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();
        String substring = requestURI.substring(requestURI.indexOf("/jd/notice") + "/jd/notice".length());
        if (substring.equals("")) {
            this.showNotice(req, resp);
        } else if (substring.equals("/add")) {
            ApiRestResponse add = this.add(req, resp);
            out.println(JSON.toJSONString(add));
        } else if (substring.equals("/edit")) {
            ApiRestResponse edit = this.edit(req, resp);
            out.println(JSON.toJSONString(edit));
        } else if (substring.equals("/delete")) {
            ApiRestResponse delete = this.delete(req, resp);
            out.println(JSON.toJSONString(delete));
        } else if (substring.equals("/list")) {
            ApiRestResponse apiRestResponse = this.noticeList(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else {
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
        }
    }
    public void showNotice(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> userMenuTreeVoSet = userService.buttonPermission(user.getUserId(), "公告管理");
        req.setAttribute("buttons", userMenuTreeVoSet);

        req.getRequestDispatcher("/WEB-INF/jsp/notice/notice.jsp").forward(req, resp);

    }

    public ApiRestResponse add(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject json = null;
        try {
            json = MyIOUtils.getJson(req);
            Notice notice = JSONObject.parseObject(JSON.toJSONString(json), Notice.class);
            noticeService.add(notice);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

    public ApiRestResponse noticeList(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> paging = new LinkedHashMap<>();
        try {
            String title = req.getParameter("title");
            String content = req.getParameter("content");
            Integer pageNum = Integer.valueOf(req.getParameter("page"));
            Integer pageSize = Integer.valueOf(req.getParameter("limit"));
            paging = noticeService.paging(title,content, pageNum, pageSize);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success(paging);
    }

    public ApiRestResponse edit(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject json = null;
        try {
            json = MyIOUtils.getJson(req);
            Notice notice = JSONObject.parseObject(JSON.toJSONString(json), Notice.class);
            noticeService.edit(notice);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (IOException e) {
            return ApiRestResponse.error(e.getMessage());
        }

        return ApiRestResponse.success();
    }

    public ApiRestResponse delete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String categoryId = req.getParameter("noticeId");
             noticeService.delete(Long.valueOf(categoryId));
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

}
