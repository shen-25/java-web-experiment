package com.jingdong.manager.model.request;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * @author word
 */
public class OrderAndProductReq {


    private Long productId;
    private Integer quantity;
    /**
     * 订单号（非主键id）
     */
    private String orderNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单总价格
     */
    private Integer totalPrice;

    /**
     * 收货人姓名快照
     */
    private String receiverName;

    /**
     * 收货人手机号快照
     */
    private String receiverMobile;

    /**
     * 收货地址快照
     */
    private String receiverAddress;

    /**
     * 订单状态: 0用户已取消，1未付款（初始状态），2已付款，3已发货，4交易完成
     */
    private Integer orderStatus;

    /**
     * 运费，默认为0
     */
    private Integer postage;

    /**
     * 支付类型,1-在线支付
     */
    private Integer paymentType = 1;


    private Date createTime;



    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPostage() {
        return postage;
    }

    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static void main(String[] args) {
        OrderAndProductReq orderAndProductReq = new OrderAndProductReq();
        orderAndProductReq.setProductId(3L);
        orderAndProductReq.setUserId(3L);
        orderAndProductReq.setQuantity(18);

        orderAndProductReq.setPostage(0);
        orderAndProductReq.setReceiverName("曾深");
        orderAndProductReq.setReceiverMobile("19521785152");
        orderAndProductReq.setReceiverAddress("东莞理工莞华社区");
        System.out.println(JSON.toJSONString(orderAndProductReq));
    }
}
