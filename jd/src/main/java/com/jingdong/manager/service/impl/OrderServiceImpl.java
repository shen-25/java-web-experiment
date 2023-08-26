package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.CartCommand;
import com.jingdong.manager.command.OrderCommand;
import com.jingdong.manager.command.OrderItemCommand;
import com.jingdong.manager.command.ProductCommand;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Cart;
import com.jingdong.manager.model.entity.Order;
import com.jingdong.manager.model.entity.OrderItem;
import com.jingdong.manager.model.entity.Product;
import com.jingdong.manager.model.request.CartOrderReq;
import com.jingdong.manager.model.request.OrderAndProductReq;
import com.jingdong.manager.model.vo.OrderVo;
import com.jingdong.manager.service.OrderService;
import com.jingdong.manager.service.ProductService;
import com.jingdong.manager.utils.DateFormatUtils;
import com.jingdong.manager.utils.OrderCodeFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private OrderCommand orderCommand = new OrderCommand();
    private ProductService productService = new ProductServiceImpl();
    private ProductCommand productCommand = new ProductCommand();
    private OrderItemCommand orderItemCommand = new OrderItemCommand();
    private CartCommand cartCommand = new CartCommand();

    @Override
    public void add(OrderAndProductReq orderAndProductReq) {

        validProduct(orderAndProductReq.getProductId(), orderAndProductReq.getQuantity());
        Order order1 = orderCommand.selectByOrderNo(orderAndProductReq.getOrderNo());
        if (order1 != null) {
            throw new BusinessException(BusinessExceptionEnum.CATEGORY_NAME_EXIST);
        }
        Long productId = orderAndProductReq.getProductId();

        Product product = productCommand.selectById(productId);
        if (product == null) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXIST);
        }

        String orderCode = OrderCodeFactory.getOrderCode((long) orderAndProductReq.getUserId());
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderNo(orderCode);
        orderItem.setProductId(product.getProductId());
        orderItem.setProductName(product.getProductName());
        orderItem.setProductImg(product.getProductPicUrl());
        orderItem.setUnitPrice(product.getProductPrice());
        orderItem.setQuantity(orderAndProductReq.getQuantity());
        Integer totalPrice = product.getProductPrice() * orderAndProductReq.getQuantity();
        orderItem.setTotalPrice(totalPrice);
        orderItem.setCreateTime(new Date());
        orderItemCommand.insert(orderItem);

        Order order = new Order();
        order.setUserId(orderAndProductReq.getUserId());
        order.setOrderNo(orderCode);
        order.setOrderStatus(Constant.OrderStatus.NOT_PAID);
        order.setTotalPrice(totalPrice);
        order.setReceiverName(orderAndProductReq.getReceiverName());
        order.setReceiverMobile(orderAndProductReq.getReceiverMobile());
        order.setReceiverAddress(orderAndProductReq.getReceiverAddress());
        order.setPostage(0);
        order.setPaymentType(orderAndProductReq.getPaymentType());
        order.setCreateTime(new Date());
        orderCommand.insert(order);
        product.setProductStock(product.getProductStock() - orderAndProductReq.getQuantity() );
        productCommand.updateById(product);
    }

    @Override
    public Order selectById(Long orderId) {
        return orderCommand.selectById(orderId);
    }

    @Override
    public void pay(String orderNo) {
        Order order = orderCommand.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(BusinessExceptionEnum.ORDER_NOT_EXIST);
        }
        order.setOrderStatus(Constant.OrderStatus.PAID);
        order.setPayTime(new Date());
        order.setUpdateTime(new Date());

        orderCommand.edit(order);

    }

    @Override
    public void cancel(String orderNo) {
        Order order = orderCommand.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(BusinessExceptionEnum.ORDER_NOT_EXIST);
        }
        order.setOrderStatus(Constant.OrderStatus.CANCELED);
        order.setUpdateTime(new Date());

        orderCommand.edit(order);
    }

    @Override
    public void deliver(String orderNo, String deliveryNo, String company, Integer postage) {
        Order order = orderCommand.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(BusinessExceptionEnum.ORDER_NOT_EXIST);
        }
        order.setUpdateTime(new Date());
        order.setOrderStatus(Constant.OrderStatus.DELIVERED);
        order.setDeliveryNo(deliveryNo);
        order.setDeliveryCompany(company);
        order.setPostage(postage);
        order.setDeliveryTime(new Date());
        orderCommand.edit(order);
    }

    @Override
    public void finish(String orderNo) {
        Order order = orderCommand.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(BusinessExceptionEnum.ORDER_NOT_EXIST);
        }
        order.setUpdateTime(new Date());
        order.setEndTime(new Date());
        order.setOrderStatus(Constant.OrderStatus.FINISHED);
        orderCommand.edit(order);
    }


    @Override
    public void delete(String orderNo) {
        Order order = orderCommand.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(BusinessExceptionEnum.ORDER_NOT_EXIST);
        }
        orderItemCommand.deleteByOrderNo(orderNo);
        orderCommand.deleteById(order.getId());
    }

    @Override
    public Map<String, Object> selectPage(String orderNo, String receiverName, String createTime, String orderStatus, Integer pageNum, Integer pageSize) {
        return orderCommand.selectPage(orderNo, receiverName, createTime, orderStatus, pageNum, pageSize);
    }

    @Override
    public OrderVo orderToOrderVo(Order order) {
        OrderVo orderVo = new OrderVo();
        orderVo.setId(order.getId());
        orderVo.setOrderNo(order.getOrderNo());
        Double totalPrice = 1.0 * order.getTotalPrice() / Constant.tightProductNumber;
        String  str = String.format("%.2f", totalPrice);
        orderVo.setTotalPrice(str);
        orderVo.setUserId(order.getUserId());
        orderVo.setReceiverName(order.getReceiverName());
        orderVo.setReceiverMobile(order.getReceiverMobile());
        orderVo.setReceiverAddress(order.getReceiverAddress());

        orderVo.setOrderStatusName(getOrderStatusName(order.getOrderStatus()));
        Double postagePrice = 1.0 * order.getPostage() / Constant.tightProductNumber;
        String  postagePriceStr = String.format("%.2f", postagePrice);
        Double  post = Double.parseDouble(postagePriceStr);
        orderVo.setPostage(post);
        orderVo.setPaymentTypeName(getOrderPaymentName(order.getPaymentType()));
        orderVo.setDeliveryNo(order.getDeliveryNo());
        orderVo.setDeliveryCompany(order.getDeliveryCompany());
        orderVo.setDeliveryTime(DateFormatUtils.dateToStr(order.getDeliveryTime()));
        orderVo.setPayTime(DateFormatUtils.dateToStr(order.getPayTime()));
        orderVo.setEndTime(DateFormatUtils.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateFormatUtils.dateToStr(order.getCreateTime()));
        orderVo.setUpdateTime(DateFormatUtils.dateToStr(order.getUpdateTime()));

        return orderVo;
    }

    @Override
    public void validProduct(Long productId, Integer count) {
        Product product = productCommand.selectById(productId);
        if (product == null || product.getProductState().equals(Constant.ProductState.STOP)) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_SALE);
        }
        if (product.getProductStock() < count) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_COUNT);
        }

    }

    @Override
    public void cartToOrder(CartOrderReq cartOrderReq) {
        List<Cart> cartList = cartCommand.selectByUserId(cartOrderReq.getUserId());
        List<Cart> cartList1 = new ArrayList<>();
        for (Cart cart : cartList) {
            if (cart.getSelected().equals(Constant.CartState.SELECTED)) {
                cartList1.add(cart);
            }
        }
        if ( cartList1.isEmpty()) {
            throw new BusinessException(BusinessExceptionEnum.CART_NOT_EXITS);
        }
        String orderNo = OrderCodeFactory.getOrderCode(cartOrderReq.getUserId());
        validSaleStatusAndStock(cartList1);

        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartList1, orderNo);

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            Product product = productCommand.selectById(orderItem.getProductId());
            int stock = product.getProductStock() - orderItem.getQuantity();
            if (stock < 0) {
                throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_COUNT);
            }
            product.setProductStock(stock);
            productCommand.updateById(product);
        }
        cleanCart(cartList1);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(cartOrderReq.getUserId());
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverName(cartOrderReq.getReceiverName());
        order.setReceiverMobile(cartOrderReq.getReceiverMobile());
        order.setReceiverAddress(cartOrderReq.getReceiverAddress());
        order.setOrderStatus(Constant.OrderStatus.NOT_PAID);
        order.setPostage(cartOrderReq.getPostage());
        order.setCreateTime(new Date());
        order.setPaymentType(cartOrderReq.getPaymentType());
        orderCommand.insert(order);
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            orderItemCommand.insert(orderItem);
        }

    }

    public  Integer totalPrice(  List<OrderItem> orderItemList){
        Integer res = 0;
        for (OrderItem orderItem : orderItemList) {
            Integer ans = orderItem.getQuantity() * orderItem.getUnitPrice();
            res += ans;
        }
        return res;
    }
    public void cleanCart(List<Cart> cartList) {
        for (Cart cart : cartList) {
            cartCommand.deleteById(cart.getId());
        }
    }

    private void validSaleStatusAndStock(List<Cart> cartList) {
        for (int i = 0; i < cartList.size(); i++) {
            Cart cart = cartList.get(i);
            Product product = productCommand.selectById(cart.getProductId());
            if (product == null || product.getProductState().equals(Constant.ProductState.STOP)) {
                throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_SALE);
            }
            if (product.getProductStock() < cart.getQuantity()) {
                throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_COUNT);
            }
        }
    }

    private List<OrderItem> cartVOListToOrderItemList(List<Cart> cartList, String orderNo) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Cart cart : cartList) {
            Product product = productCommand.selectById(cart.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getProductId());
            orderItem.setProductName(product.getProductName());
            orderItem.setProductImg(product.getProductPicUrl());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setUnitPrice(product.getProductPrice());
            orderItem.setTotalPrice(product.getProductPrice() * cart.getQuantity());
            orderItem.setCreateTime(new Date());
            orderItem.setOrderNo(orderNo);
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private String getOrderStatusName(Integer status) {
        String statusName = "";
        if (status == Constant.OrderStatus.CANCELED) {
            statusName = "用户取消";
        } else if(status == Constant.OrderStatus.NOT_PAID){
            statusName = "未付款";
        }else if (status == Constant.OrderStatus.PAID) {
            statusName = "已付款";
        }else if(status == Constant.OrderStatus.DELIVERED){
            statusName = "已发货";
        }else if (status == Constant.OrderStatus.FINISHED) {
            statusName = "交易完成";
        }
        return statusName;
    }

    private String getOrderPaymentName(Integer status) {
        String statusName = "";
        if (status == Constant.OrderPaymentState.ON_LINE) {
            statusName = "在线支付";
        } else if (status == Constant.OrderPaymentState.UP_LINE) {
            statusName = "线下付款";
        }
        return statusName;
    }
}
