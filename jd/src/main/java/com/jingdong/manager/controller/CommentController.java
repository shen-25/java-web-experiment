package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.model.entity.Comment;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.CommentService;
import com.jingdong.manager.service.UserService;
import com.jingdong.manager.service.impl.CommentServiceImpl;
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

@WebServlet(name = "commentController", value = "/comment/*")
public class CommentController extends HttpServlet {
    private UserService userService = new UserServiceImpl();

    private CommentService commentService = new CommentServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();
        String substring = requestURI.substring(requestURI.indexOf("/jd/comment") + "/jd/comment".length());
        if (substring.equals("")) {
            this.showComment(req, resp);
        } else if (substring.equals("/list")) {
            ApiRestResponse apiRestResponse = this.commentList(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        }
        else if (substring.equals("/add")) {
            ApiRestResponse add = this.add(req, resp);
            out.println(JSON.toJSONString(add));
        } else if (substring.equals("/edit")) {
            ApiRestResponse reply = this.replyOrStatus(req, resp);
            out.println(JSON.toJSONString(reply));
        }else if (substring.equals("/delete")) {
            ApiRestResponse delete = this.delete(req, resp);
            out.println(JSON.toJSONString(delete));
        }  else {
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
        }
    }

    public void showComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> userMenuTreeVoSet = userService.buttonPermission(user.getUserId(), "评论管理");
        req.setAttribute("buttons", userMenuTreeVoSet);
        req.getRequestDispatcher("/WEB-INF/jsp/comment/comment.jsp").forward(req, resp);

    }

    public  ApiRestResponse commentList(HttpServletRequest req, HttpServletResponse resp){
        Map<String, Object> paging = new LinkedHashMap<>();
        try {
            String userName = req.getParameter("userName");
            String productName = req.getParameter("productName");
            String createTime = req.getParameter("createTime");
            String statusString = req.getParameter("status");
            Integer statusInteger = null;
            if(statusString != null && statusString != "") {
               statusInteger =Integer.valueOf(statusString);
            }
            Integer pageNum = Integer.valueOf(req.getParameter("page"));
            Integer pageSize = Integer.valueOf(req.getParameter("limit"));
            paging = commentService.paging(userName, productName, createTime, statusInteger, pageNum, pageSize);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success(paging);
    }

    public ApiRestResponse add(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject json = null;
        try {
            json = MyIOUtils.getJson(req);
            Comment comment = JSONObject.parseObject(JSON.toJSONString(json), Comment.class);
            commentService.add(comment);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

    public ApiRestResponse replyOrStatus(HttpServletRequest req, HttpServletResponse resp) {
        try {

            Long commentId = Long.valueOf(req.getParameter("commentId"));
            String reply = req.getParameter("reply");
            Integer status = Integer.valueOf(req.getParameter("status"));
            commentService.reply(commentId, reply, status);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }

        return ApiRestResponse.success();
    }

    public ApiRestResponse delete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String commentId = req.getParameter("commentId");
            commentService.delete(Long.valueOf(commentId));
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }



}
