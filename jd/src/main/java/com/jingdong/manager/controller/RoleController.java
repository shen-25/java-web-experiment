package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.model.entity.Role;
import com.jingdong.manager.model.entity.User;

import com.jingdong.manager.model.vo.RoleKeyValueVo;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.RoleService;
import com.jingdong.manager.service.UserService;
import com.jingdong.manager.service.impl.RoleServiceImpl;
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
import java.util.List;
import java.util.Map;

/**
 * @author word
 */

@WebServlet(name = "roleController", value = "/sys/role/*")
public class RoleController extends HttpServlet {

    private RoleService roleService = new RoleServiceImpl();
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
        String substring = requestURI.substring(requestURI.indexOf("/jd/sys/role") + "/jd/sys/role".length());
        if (substring.equals("/role_key_value")) {
            ApiRestResponse apiRestResponse = roleKeyValue();
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("")) {
            this.role(req, resp);
        } else if (substring.equals("/list")) {
            ApiRestResponse apiRestResponse = this.roleList(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("/add")) {
            ApiRestResponse add = this.add(req, resp);
            out.println(JSON.toJSONString(add));
        } else if (substring.equals("/edit")) {
            ApiRestResponse edit = this.edit(req, resp);
            out.println(JSON.toJSONString(edit));
        } else if (substring.equals("/delete")) {
            ApiRestResponse delete = this.delete(req, resp);
            out.println(JSON.toJSONString(delete));
        } else if (substring.equals("/tree")) {
            this.permissionTree(req, resp);
        } else if(substring.equals("/buttonPermission")){
            ApiRestResponse apiRestResponse = this.buttonPermission(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        }else{
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);

        }
    }

    public ApiRestResponse roleKeyValue() {
        List<RoleKeyValueVo> roleKeyValueVoList = null;
        try {
            roleKeyValueVoList = roleService.selectRoleKeyValue();
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success(roleKeyValueVoList);
    }

    public void role(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> roleMenuTreeVoSet = userService.buttonPermission(user.getUserId(), "角色管理");
        req.setAttribute("buttons", roleMenuTreeVoSet);
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/role/role.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ApiRestResponse roleList(HttpServletRequest req, HttpServletResponse resp ) {
       String roleName = req.getParameter("roleName");
        Integer pageNum = Integer.valueOf(req.getParameter("page"));
        Integer pageSize = Integer.valueOf(req.getParameter("limit"));
        Map<String, Object> paging = roleService.paging(roleName, pageNum, pageSize);
        return ApiRestResponse.success(paging);
    }

    public ApiRestResponse add(HttpServletRequest req, HttpServletResponse resp) {
        try {
            JSONObject json = MyIOUtils.getJson(req);
            Role role = JSONObject.parseObject(JSON.toJSONString(json), Role.class);
            roleService.add(role);
        }catch (BusinessException | IOException e) {

        }
        return ApiRestResponse.success();
    }


    public ApiRestResponse edit(HttpServletRequest req, HttpServletResponse resp) {
        try {
            JSONObject json = MyIOUtils.getJson(req);
            Role role = JSONObject.parseObject(JSON.toJSONString(json), Role.class);
            roleService.edit(role);
        }catch (BusinessException | IOException e) {

        }
        return ApiRestResponse.success();
    }

    public ApiRestResponse delete(HttpServletRequest req, HttpServletResponse resp) {
        Long roleId = Long.valueOf(req.getParameter("roleId"));
        roleService.delete(roleId);
        return ApiRestResponse.success();
    }


    public void permissionTree(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/role/tree.jsp").forward(req, resp);

    }

    public ApiRestResponse buttonPermission(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> buttonPermission = userService.buttonPermission(user.getUserId(), "角色管理");
        return ApiRestResponse.success(buttonPermission);
    }
}
