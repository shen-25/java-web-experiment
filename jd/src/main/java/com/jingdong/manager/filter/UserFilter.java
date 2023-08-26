package com.jingdong.manager.filter;


import com.jingdong.manager.common.Constant;
import com.jingdong.manager.model.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * @author word
 * 不用的
 */
public class UserFilter implements Filter {

    private static User currentUser;

    private static final List<String> ALLOWED_PATHS =
            Arrays.asList("/sys/user/login.jsp", "/sys/user/check_login", "/sys/user/verify_code");

    private static final List<String> STATICRESOURCE =
            Arrays.asList("css", "png", "jpg", "js", "woff2?v=256", "ico", "ttf", "woff", "woff2");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        if (ALLOWED_PATHS.contains(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if(STATICRESOURCE.contains(requestURI)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpSession session = request.getSession();
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        currentUser = (User) session.getAttribute(Constant.CURRENT_USER);
        if (currentUser == null) {
            PrintWriter out = new HttpServletResponseWrapper(response).getWriter();
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"NEED_LOGIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            response.sendRedirect("/sys/user/login.jsp");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }



    @Override
    public void destroy() {

    }
}
