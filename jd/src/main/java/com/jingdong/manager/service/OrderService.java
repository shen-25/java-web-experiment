package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Order;
import com.jingdong.manager.model.request.CartOrderReq;
import com.jingdong.manager.model.request.OrderAndProductReq;
import com.jingdong.manager.model.vo.OrderVo;

import java.util.Map;

public interface OrderService {

    public void add(OrderAndProductReq orderAndProductReq);

    public Order selectById(Long orderId);

    public  void pay(String orderNo);

    void cancel(String orderNo);


    public void deliver(String orderNo, String deliveryNo, String company, Integer postage);

    public void finish(String orderNo);

    public void delete(String orderNo);

    public Map<String, Object> selectPage(String orderNo, String receiverName , String createTime, String orderStatus, Integer pageNum, Integer pageSize);

     public OrderVo orderToOrderVo(Order order);

     public  void validProduct(Long productId, Integer count);

    public void cartToOrder(CartOrderReq cartOrderReq);

}
