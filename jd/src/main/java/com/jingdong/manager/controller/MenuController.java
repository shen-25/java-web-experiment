package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.model.entity.Menu;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.request.MenuSelectReq;
import com.jingdong.manager.model.vo.PermissionTreeVo;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.MenuService;
import com.jingdong.manager.service.UserService;
import com.jingdong.manager.service.impl.MenuServiceImpl;
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

/**
 * @author word
 */
@WebServlet(name = "menuController", value = "/sys/menu/*")
public class MenuController extends HttpServlet {


    private MenuService menuService = new MenuServiceImpl();

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
        String substring = requestURI.substring(requestURI.indexOf("/jd/sys/menu") + "/jd/sys/menu".length());
        if (substring.equals("")) {
            this.menu(req, resp);
        } else if (substring.equals("/list")) {
            ApiRestResponse apiRestResponse = this.menuList(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("/add")) {
            ApiRestResponse add = this.add(req, resp);
            out.println(JSON.toJSONString(add));
        } else if (substring.equals("/edit")) {
            ApiRestResponse edit = this.edit(req, resp);
            out.println(JSON.toJSONString(edit));
        } else if (substring.equals("/permission/tree")) {
            ApiRestResponse apiRestResponse = this.permissionForAllTree();
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("/all/menuSelect")) {
            ApiRestResponse apiRestResponse = this.menuNameSelect();
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("/delete")) {
            ApiRestResponse delete = this.delete(req, resp);
            out.println(JSON.toJSONString(delete));
        } else if (substring.substring(0, substring.lastIndexOf("/")).equals("/permission/tree/menu")) {
            permissionForTreeMenu(req, resp);
        }else{
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);

        }
    }


    public void menu(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session = req.getSession();
        List<MenuSelectReq> menuSelectReqList = menuService.menuNameSelect();
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> roleMenuTreeVoSet = userService.buttonPermission(user.getUserId(), "菜单管理");
        req.setAttribute("buttons",roleMenuTreeVoSet);
        req.setAttribute("menuSelectReqList", menuSelectReqList);
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/menu/menu.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ApiRestResponse menuList(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> paging = new LinkedHashMap<>();
        try {
            String menuName = req.getParameter("menuName");
            String url = req.getParameter("url");
            String menuState = req.getParameter("menuState");
            Integer pageNum = Integer.valueOf(req.getParameter("page"));
            Integer pageSize = Integer.valueOf(req.getParameter("limit"));
            paging = menuService.paging(menuName, url, menuState, pageNum, pageSize);
        }catch (BusinessException e) {
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
            Menu menu = JSONObject.parseObject(JSON.toJSONString(json), Menu.class);
            menuService.add(menu);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (IOException e) {
            return ApiRestResponse.error(e.getMessage());
        }catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

    public ApiRestResponse edit(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject json = null;
        try {
            json = MyIOUtils.getJson(req);
            Menu menu = JSONObject.parseObject(JSON.toJSONString(json), Menu.class);
            menuService.edit(menu);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (IOException e) {
            return ApiRestResponse.error(e.getMessage());
        }catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }

        return ApiRestResponse.success();
    }


    public ApiRestResponse permissionForAllTree() {
        List<PermissionTreeVo> permissionTreeVoList = null;
        try {
            permissionTreeVoList = menuService.permissionTreeForAll();
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success(permissionTreeVoList);
    }

    public ApiRestResponse permissionForTreeMenu(HttpServletRequest req, HttpServletResponse resp) {
        List<PermissionTreeVo> permissionTreeVoList = null;
        try {
            String requestURI = req.getRequestURI();
            String str = requestURI.substring(requestURI.lastIndexOf("/") + 1);
            Long menuId = Long.valueOf(str);
            permissionTreeVoList = menuService.permissionTree(menuId);
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success(permissionTreeVoList);
    }

    public ApiRestResponse menuNameSelect() {
        List<MenuSelectReq> menuSelectReqList = null;
        try {
            menuSelectReqList = menuService.menuNameSelect();
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success(menuSelectReqList);
    }

    public ApiRestResponse delete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String menuId = req.getParameter("menuId");
            menuService.delete(menuId);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        }catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }
}
