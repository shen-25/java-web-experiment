<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/11/21
  Time: 上午 09:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="/jd/resources/layui/css/layui.css" />
    <link rel="stylesheet" href="/jd/css/index.css" />
    <link rel="stylesheet" href="/jd/css/orderDetail.css" />
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <title>订单详情</title>
</head>
<body>
<div class="main">
    <div class="top">
        <div class="top__left">
            <i class="layui-icon" style="font-size: 20px">&#xe667;</i>
            当前订单状态：${orderVo.orderStatusName}
        </div>
        <div class="top__right">
            <button
                    type="submit"
                    id="delete-order"
                    onclick="deleteOrder('${orderVo.orderNo}')"
                    class="layui-btn layui-btn-primary layui-btn-sm layui-btn-fluid"
            >
                删除订单
            </button>
        </div>
    </div>
    <div class="content">
        <div class="content_order mb20">
            <div class="order_title">
                <i class="layui-icon" style="font-size: 20px"> &#xe715;</i>
                基本信息
            </div>
            <div class="order_table">
                <table class="layui-table">
                    <thead>
                    <tr>
                        <th>订单编号</th>
                        <th>用户账号</th>
                        <th>支付类型</th>
                        <th>快递公司</th>
                        <th>物流单号</th>
                        <th>运费</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>

                        <td>${orderVo.orderNo}</td>
                        <td>${orderVo.userId}</td>
                        <td>${orderVo.paymentTypeName}</td>
                        <td>${orderVo.deliveryCompany}</td>
                        <td>${orderVo.deliveryNo}</td>
                        <td>￥${orderVo.postage}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="order_table">
                <table class="layui-table">
                    <thead>
                    <tr>
                        <th>发货时间</th>
                        <th>支付时间</th>
                        <th>交易完成时间</th>
                        <th>创建时间</th>
                        <th>更新时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${orderVo.deliveryTime}</td>
                        <td>${orderVo.payTime}</td>
                        <td>${orderVo.endTime}</td>

                        <td>${orderVo.createTime}</td>
                        <td>${orderVo.updateTime}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="content_order mb20">
            <div class="order_title">
                <i class="layui-icon" style="font-size: 20px"> &#xe715;</i>
                收货人信息
            </div>
            <div class="order_table">
                <table class="layui-table">
                    <thead>
                    <tr>
                        <th>收货人</th>
                        <th>手机号码</th>
                        <th>收货地址</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${orderVo.receiverName}</td>
                        <td>${orderVo.receiverMobile}</td>
                        <td>
                            ${orderVo.receiverAddress}
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="content_order mb20">
            <div class="order_title">
                <i class="layui-icon" style="font-size: 20px"> &#xe715;</i>
                商品信息
            </div>
            <div class="order_table">
                <table class="layui-table">
                    <thead>
                    <tr>
                        <th>商品图片</th>
                        <th>商品名称</th>
                        <th>单价</th>
                        <th>数量</th>
                        <th>小计</th>
                    </tr>
                    </thead>
                    <tbody>
                 <c:forEach items="${orderItemList}" var="orderItem">
                    <tr>
                        <td><img src="${orderItem.productImg}" alt=""/></td>
                        <td>${orderItem.productName}</td>
                        <td>￥${orderItem.unitPrice / 100}</td>
                        <td>${orderItem.quantity}</td>
                        <td>￥${orderItem.totalPrice / 100}</td>
                    </tr>
                 </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="order-total">合计：<span>￥${orderVo.totalPrice}</span>

            </div>
        </div>
    </div>
</div>
</div>
<script>

    function deleteOrder(orderNo){
        layer.confirm("你确定要删除编号为[" + orderNo + "]的订单吗？", {icon:3},
            function(index){
                layer.close(index);
                $.ajax({
                    url: "/jd/order/delete",
                    data: {"orderNo": orderNo},
                    type: 'post',
                    dataType: 'json',
                    success:  function (json) {
                        if (json.code === 200) {
                            layer.msg("删除成功", {
                                icon: 1,
                                time: 1300,
                                offset: 't',
                            });
                          setTimeout(function (){
                              window.location.href = "/jd/order";
                          }, 1000)
                        } else{
                            layer.msg(json.msg, {
                                icon: 2,
                                anim: 6,
                                time: 1300,
                                offset: 't',
                            });
                        }
                    }
                });
            });

    }

</script>
</body>

</html>
