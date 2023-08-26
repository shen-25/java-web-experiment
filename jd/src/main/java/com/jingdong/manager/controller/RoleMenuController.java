package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.service.RoleMenuService;
import com.jingdong.manager.service.impl.RoleMenuServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author word
 */

@WebServlet(name = "roleMenuController", value = "/sys/role_menu/*")
public class RoleMenuController extends HttpServlet {


    private RoleMenuService roleMenuService = new RoleMenuServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();
        String substring = requestURI.substring(requestURI.indexOf("/jd/sys/role_menu") + "/jd/sys/role_menu".length());
        if (substring.equals("/role/permission")) {
            ApiRestResponse rolePermission = this.getRolePermission(req, resp);
            out.println(JSON.toJSONString(rolePermission));

        } else if (substring.equals("/role/setPermission")) {
            ApiRestResponse apiRestResponse = this.setPermission(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("/delete")) {
            ApiRestResponse delete = this.delete(req, resp);
            out.println(JSON.toJSONString(delete));
        }else{
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);

        }
    }


    public ApiRestResponse getRolePermission(HttpServletRequest req, HttpServletResponse resp) {
        List<Long> permission = null;
        try {
            Long roleId = Long.valueOf(req.getParameter("roleId"));
            permission  = roleMenuService.getRolePermission(roleId);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }

        return ApiRestResponse.success(permission);

    }


    public ApiRestResponse setPermission(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String str = req.getParameter("data");
            Long roleId = Long.valueOf(req.getParameter("roleId"));
            List<Long> list = roleMenuService.setPermission(str, roleId);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

    public ApiRestResponse delete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Long roleId = Long.valueOf(req.getParameter("roleId"));
            roleMenuService.delete(roleId);
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }
}
