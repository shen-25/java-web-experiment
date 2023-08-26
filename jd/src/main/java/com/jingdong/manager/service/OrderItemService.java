package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.OrderItem;

import java.util.List;

public interface OrderItemService {

    public List<OrderItem> selectByOrderNo(String orderNo);
    public void deleteByOrderNo(String orderNo);
    public void insert(OrderItem orderItem);

}
