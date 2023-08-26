package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.RoleUserService;
import com.jingdong.manager.service.impl.RoleUserServiceImpl;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@WebServlet(name = "roleUserController", value = "/sys/role_user/*")
public class RoleUserController extends HttpServlet {

    private RoleUserService roleUserService = new RoleUserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();
        String substring = requestURI.substring(requestURI.indexOf("/jd/sys/role_user") + "/jd/sys/role_user".length());
        if (substring.equals("/select_roles")) {
            ApiRestResponse apiRestResponse = this.selectRoles(req, resp);
            out.print(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("/delete")) {
            ApiRestResponse delete = this.delete(req, resp);
            out.print(JSON.toJSONString(delete));
        } else if (substring.equals("/create")) {
            ApiRestResponse apiRestResponse = this.create(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("/menu/userMenuList")) {
            ApiRestResponse apiRestResponse = this.menuList(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("update")) {
            ApiRestResponse update = this.update(req, resp);
            out.println(JSON.toJSONString(update));
        } else{
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);

        }
    }


    public ApiRestResponse selectRoles(HttpServletRequest req, HttpServletResponse resp){
        List<Long> list = null;
        try {
            Long userId = Long.valueOf(req.getParameter("userId"));
            list  = roleUserService.selectRoleIds(userId);
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(500, e.getMessage());
        }
        return ApiRestResponse.success(list);
    }


    public ApiRestResponse delete(HttpServletRequest req, HttpServletResponse resp){
        try {
            Long userId = Long.valueOf(req.getParameter("userId"));

            roleUserService.delete(userId);
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(500, e.getMessage());
        }
        return ApiRestResponse.success();
    }


    public ApiRestResponse create(HttpServletRequest req, HttpServletResponse resp) {
        JSONArray objects = JSON.parseArray(req.getParameter("roleIds"));
        Object[] objects1 = objects.stream().toArray();
        List<Long> roleIds = new ArrayList<>();
        try {
            for (Object o : objects1) {
                roleIds.add(Long.valueOf(String.valueOf(o)));
            }
            Long userId = Long.valueOf(req.getParameter("userId"));
            roleUserService.batchInsert(roleIds, userId);
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(500, e.getMessage());
        }
        return ApiRestResponse.success();

    }

    public ApiRestResponse update(HttpServletRequest req, HttpServletResponse resp) {
        JSONArray objects = JSON.parseArray(req.getParameter("roleIds"));
        Object[] objects1 = objects.stream().toArray();
        List<Long> roleIds = new ArrayList<>();
        try {
            for (Object o : objects1) {
                roleIds.add(Long.valueOf(String.valueOf(o)));
            }
            Long userId = Long.valueOf(req.getParameter("userId"));
            roleUserService.update(roleIds, userId);
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(500, e.getMessage());
        }
        return ApiRestResponse.success();
    }



    public ApiRestResponse menuList(HttpServletRequest req, HttpServletResponse resp) {
        Set<UserMenuTreeVo> userMenuTreeVos = null;
        try {
            Long userId = Long.valueOf(req.getParameter("userId"));
            userMenuTreeVos = roleUserService.menuList(userId);
        }
        catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(500, e.getMessage());
        }
        return ApiRestResponse.success(userMenuTreeVos);
    }

}
