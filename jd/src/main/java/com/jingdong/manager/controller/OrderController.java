package com.jingdong.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingdong.manager.common.ApiRestResponse;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.model.entity.Order;
import com.jingdong.manager.model.entity.OrderItem;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.model.request.CartOrderReq;
import com.jingdong.manager.model.request.OrderAndProductReq;
import com.jingdong.manager.model.vo.OrderVo;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.OrderItemService;
import com.jingdong.manager.service.OrderService;
import com.jingdong.manager.service.UserService;
import com.jingdong.manager.service.impl.OrderItemServiceImpl;
import com.jingdong.manager.service.impl.OrderServiceImpl;
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

@WebServlet(name = "orderController", value = "/order/*")

public class OrderController  extends HttpServlet {
    private OrderService orderService = new OrderServiceImpl();

    private OrderItemService orderItemService = new OrderItemServiceImpl();
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
        String substring = requestURI.substring(requestURI.indexOf("/jd/order") + "/jd/order".length());
        if (substring.equals("")) {
            this.showOrder(req, resp);
        } else if (substring.equals("/detail")) {
            this.orderDetail(req, resp);
        } else if (substring.equals("/list")) {
            ApiRestResponse apiRestResponse = orderList(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else if(substring.equals("/cart/pay")){
            ApiRestResponse apiRestResponse = this.cartToOrder(req, resp);
            out.println(JSON.toJSONString(apiRestResponse));
        } else if (substring.equals("/delivery")) {
            ApiRestResponse deliver = this.deliver(req, resp);
            out.println(JSON.toJSONString(deliver));
        } else if (substring.equals("/delete")) {
            ApiRestResponse delete = this.delete(req, resp);
            out.println(JSON.toJSONString(delete));
        } else if (substring.equals("/cancel")) {
            ApiRestResponse cancel = this.cancel(req, resp);
            out.println(JSON.toJSONString(cancel));
        }else if(substring.equals("/add")){
            ApiRestResponse add = this.add(req, resp);
            out.println(JSON.toJSONString(add));
        } else if(substring.equals("/pay")){
            ApiRestResponse add = this.pay(req, resp);
            out.println(JSON.toJSONString(add));
        } else if (substring.equals("/finish")) {
            ApiRestResponse add = this.finish(req, resp);
            out.println(JSON.toJSONString(add));
        } else {
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
        }
    }

    public ApiRestResponse pay(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String orderNo = req.getParameter("orderNo");
            orderService.pay(orderNo);
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        }
        catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

    public ApiRestResponse finish(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String orderNo = req.getParameter("orderNo");
            orderService.finish(orderNo);
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        }
        catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }
    public ApiRestResponse add(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject json = null;
        try {
            json = MyIOUtils.getJson(req);
            OrderAndProductReq orderAndProductReq = JSONObject.parseObject(JSON.toJSONString(json), OrderAndProductReq.class);
            orderService.add(orderAndProductReq);
        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        }
        catch (IOException e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();


    }

    public ApiRestResponse deliver(HttpServletRequest req, HttpServletResponse resp){
       try{
           String orderNo = req.getParameter("orderNo");
           String deliveryNo = req.getParameter("deliveryNo");
           String company = req.getParameter("deliveryCompany");
           String postageStr = req.getParameter("postage");
           Integer postage = Integer.valueOf(postageStr) * Constant.tightProductNumber;
           orderService.deliver(orderNo, deliveryNo, company, postage);
       } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

    public ApiRestResponse delete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String orderNo = req.getParameter("orderNo");
            orderService.delete((orderNo));
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

    public ApiRestResponse cancel(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String orderNo = req.getParameter("orderNo");
            orderService.cancel((orderNo));
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }


    public void showOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        List<UserMenuTreeVo> userMenuTreeVoSet = userService.buttonPermission(user.getUserId(), "订单列表");
        req.setAttribute("buttons", userMenuTreeVoSet);
        req.getRequestDispatcher("/WEB-INF/jsp/order/order.jsp").forward(req, resp);

    }

    public ApiRestResponse cartToOrder(HttpServletRequest req, HttpServletResponse resp){
        JSONObject json = null;
        try {
            json = MyIOUtils.getJson(req);
            CartOrderReq cartOrderReq = JSONObject.parseObject(JSON.toJSONString(json), CartOrderReq.class);
            orderService.cartToOrder(cartOrderReq);

        }catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        }
        catch (IOException e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success();
    }

    public void orderDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        Order order = orderService.selectById(Long.valueOf(orderId));
        String orderNo = order.getOrderNo();
        OrderVo orderVo = orderService.orderToOrderVo(order);
        List<OrderItem> orderItemList = orderItemService.selectByOrderNo(orderNo);
        req.setAttribute("orderItemList",orderItemList );
        req.setAttribute("orderVo", orderVo);

        req.getRequestDispatcher("/WEB-INF/jsp/order/orderDetail.jsp").forward(req, resp);
    }

    public ApiRestResponse orderList(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> paging = new LinkedHashMap<>();
        try {
            String orderNo = req.getParameter("orderNo");
            String receiverName = req.getParameter("receiverName");
            String orderStatus = req.getParameter("orderStatus");
            String createTime = req.getParameter("createTime");
            Integer pageNum = Integer.valueOf(req.getParameter("page"));
            Integer pageSize = Integer.valueOf(req.getParameter("limit"));
            paging = orderService.selectPage(orderNo, receiverName, createTime, orderStatus, pageNum, pageSize);
        } catch (BusinessException e) {
            return ApiRestResponse.error(e.getCode(), e.getMsg());
        } catch (Exception e) {
            return ApiRestResponse.error(e.getMessage());
        }
        return ApiRestResponse.success(paging);
    }
}
