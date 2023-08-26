package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.model.entity.Cart;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.CartService;
import com.jingdong.manager.service.UserService;
import com.jingdong.manager.service.impl.CartServiceImpl;
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

@WebServlet(name = "cartController", value = "/cart/*")
public class CartController extends HttpServlet {

    private CartService cartService = new CartServiceImpl();
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
        String substring = requestURI.substring(requestURI.indexOf("/jd/cart") + "/jd/cart".length());
        if (substring.equals("")) {
            this.showCart(req, resp);
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
            ApiRestResponse apiRestResponse = this.cartList(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else {
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
        }
    }

    public void showCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> userMenuTreeVoSet = userService.buttonPermission(user.getUserId(), "购物车管理");
        req.setAttribute("buttons", userMenuTreeVoSet);
        req.getRequestDispatcher("/WEB-INF/jsp/cart/cart.jsp").forward(req, resp);

    }

    public ApiRestResponse cartList(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> paging = new LinkedHashMap<>();
        try {
            String userId = req.getParameter("userId");
            Integer pageNum = Integer.valueOf(req.getParameter("page"));
            Integer pageSize = Integer.valueOf(req.getParameter("limit"));
            paging = cartService.paging(userId, pageNum, pageSize);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success(paging);
    }

    public ApiRestResponse add(HttpServletRequest req, HttpServletResponse resp) {
        try {
            JSONObject json =  MyIOUtils.getJson(req);
            Cart cart = JSONObject.parseObject(JSON.toJSONString(json), Cart.class);
            cartService.add(cart.getUserId(), cart.getProductId(), cart.getQuantity());
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }


    public ApiRestResponse edit(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject json = null;
        try {
            json = MyIOUtils.getJson(req);
            Cart cart = JSONObject.parseObject(JSON.toJSONString(json), Cart.class);
            cartService.update(cart.getUserId(), cart.getProductId(), cart.getQuantity());
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (IOException e) {
            return ApiRestResponse.error(e.getMessage());
        }

        return ApiRestResponse.success();
    }

    public ApiRestResponse delete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String cartId = req.getParameter("cartId");
            cartService.delete(Long.valueOf(cartId));
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }
}
