package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.OrderItemCommand;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.OrderItem;
import com.jingdong.manager.service.OrderItemService;

import java.util.List;

public class OrderItemServiceImpl implements OrderItemService {

    private OrderItemCommand orderItemCommand = new OrderItemCommand();

    @Override
    public List<OrderItem> selectByOrderNo(String orderNo) {

        List<OrderItem> orderItemCommands = orderItemCommand.selectByOrderNo(orderNo);
        return orderItemCommands;
    }

    @Override
    public void deleteByOrderNo(String orderNo) {
        List<OrderItem> orderItems= orderItemCommand.selectByOrderNo(orderNo);
        if (orderItems == null || orderItems.isEmpty()) {
            throw new BusinessException(BusinessExceptionEnum.ORDER_ITEM_NOT_EXITS);
        }
        orderItemCommand.deleteByOrderNo(orderNo);

    }

    @Override
    public void insert(OrderItem orderItem) {
        orderItemCommand.insert(orderItem);
    }
}
