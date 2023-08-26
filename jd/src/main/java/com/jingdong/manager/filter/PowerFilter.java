package com.jingdong.manager.filter;

import com.jingdong.manager.common.Constant;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.RoleUserService;
import com.jingdong.manager.service.impl.RoleUserServiceImpl;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author word
 */
public class PowerFilter implements Filter {

    private RoleUserService roleUserService = null;


    private  Set<UserMenuTreeVo> userMenuTreeVoSet = null;


    private static final List<String> EXCLUSION_URL_SET =
            Arrays.asList("/jd/sys/user/login.html", "/jd/sys/user/check_login",
                    "/jd/sys/user/verify_code",
                    "/jd/noAuth.html", "/jd/sys/user/logout");

    private static  List<String> STATIC_RESOURCES =
            Constant.STATIC_RESOURCES;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }



    private static final List<String> Resources = Arrays.asList(
                     "/jd/sys/role_menu/role/setPermission",
                    "/jd/sys/role_menu/role/permission", "/jd/sys/menu/permission/tree", "/jd/sys/role/role_key_value"
    , "/jd/sys/role_user/select_roles", "/jd/sys/user/showEdit", "/jd/sys/user/showAdd", "/jd/sys/role_user/select_roles",
    "/jd/sys/advise", "/jd/sys/advise/list", "/jd/home");



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        HttpSession session = req.getSession();
        String requestURI = req.getRequestURI();
        if(EXCLUSION_URL_SET.contains(requestURI)){
            chain.doFilter(request, response);
            return;
        }
        int index = requestURI.lastIndexOf(".");
        String str = requestURI.substring(requestURI.lastIndexOf(".") + 1);
        if(index != - 1 && STATIC_RESOURCES.contains(str)){
            chain.doFilter(request, response);
            return;
        }
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if (user == null) {
            noLogin(req, res);
            return;
        }
        roleUserService = new RoleUserServiceImpl();
        userMenuTreeVoSet  = this.roleUserService.menuList(user.getUserId());
        if (roleUserService.isSuperAdmin(user.getUserId())) {
            chain.doFilter(request, response);
            return;
        }
        if (Resources.contains(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        System.out.println(requestURI);
        if(userMenuTreeVoSet.isEmpty() || userMenuTreeVoSet == null){
            chain.doFilter(request, response);
            return;
        }
        String requestURISub = requestURI.substring(requestURI.lastIndexOf("/jd") + "/jd".length());
        System.out.println(requestURISub);
       
        for (UserMenuTreeVo userMenuTreeVo : userMenuTreeVoSet) {
            if (userMenuTreeVo.getUrl().equals(requestURISub)) {
                chain.doFilter(request, response);
                return;
            }
        }
        req.getRequestDispatcher("/jd/noAuth.html").forward(req, res);
    }


    @Override
    public void destroy() {

    }

    private void noLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        out.write("{\n" +
                "    \"status\": 10007,\n" +
                "    \"msg\": \"NEED_LOGIN\",\n" +
                "    \"data\": null\n" +
                "}");
        out.write("<h1>正在跳转至登录页面</h1>");
        out.write("<script type=\"text/javascript\">  setTimeout(function(){ window.location = \"/jd/sys/user/login.html\"; },1500);\n</script>");
        return;

    }
}
