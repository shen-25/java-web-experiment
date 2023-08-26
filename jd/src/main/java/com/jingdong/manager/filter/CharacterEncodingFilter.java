package com.jingdong.manager.filter;

import com.jingdong.manager.common.Constant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author word
 */
public class CharacterEncodingFilter implements Filter {

    private static  List<String> STATIC_RESOURCES =
            Constant.STATIC_RESOURCES;
    private static String encoding = "utf-8";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = ((HttpServletRequest)request).getRequestURI();
        int index = requestURI.lastIndexOf(".");
        String str = requestURI.substring(requestURI.lastIndexOf(".") + 1);
        if(index != - 1 && STATIC_RESOURCES.contains(str)){
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest req =  (HttpServletRequest)request;
        req.setCharacterEncoding(encoding);
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setContentType("text/html;charset=" + encoding);
        chain.doFilter(req, resp);

    }

    @Override
    public void destroy() {

    }
}
