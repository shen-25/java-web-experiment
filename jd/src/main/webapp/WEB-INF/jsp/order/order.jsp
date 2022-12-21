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
    <link rel="stylesheet" href="/jd/css/order.css" />

    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <title>订单管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form" >

        <div class="layui-inline">
            <label class="layui-form-label inline-name">订单编号</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="orderNo"
                        placeholder="请输入订单编号"
                        lay-verify="text"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label inline-name">收货人</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="receiverName"
                        placeholder="请输入收货人"
                        lay-verify="text"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline my-date-input">
            <label class="layui-form-label">创建时间</label>
            <div class="layui-input-inline">
                <input type="date" name="createTime"
                       placeholder="请输入创建时间"
                       lay-verify="text"
                       autocomplete="off"
                       class="layui-input inline-input">
            </div>
        </div>

        <div class="layui-inline  dingdang-state-style">
            <label class="layui-form-label inline-name">订单状态</label>
            <div class="layui-input-inline">
                <select name="orderStatus" >
                    <option value="-1">全部</option>
                    <option value="0">取消</option>
                    <option value="1">未付款</option>
                    <option value="2">已付款</option>
                    <option value="3">已发货</option>
                    <option value="4">交易完成</option>
                </select>
            </div>
        </div>
        <div class="layui-inline query-from-style-margin-top query-btn-style">
            <div class="layui-input-block inline-input inline-style">
                <button class="layui-btn" lay-submit
                        lay-filter="orderQueryFilter">查询</button>
                <button type="reset" class="layui-btn layui-btn-primary">
                    重置
                </button>
            </div>
        </div>
    </form>
</div>
<div class="base-table">
    <table id="orderTable" lay-filter="orderOpera" class="orderTable"></table>

    <script type="text/html" id="orderBar">
        <c:forEach items="${buttons}" var="btn">
          <c:if test="${btn.menuName == '查看'}">
            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="view">${btn.menuName}</a>
        </c:if>
            <c:if test="${btn.menuName == '发货'}">
                {{#  if(d.orderStatus == 2){ }}
                <a class="layui-btn  layui-btn-normal layui-btn-xs" lay-event="delivery">${btn.menuName}</a>
                {{#  } }}
            </c:if>
            <c:if test="${btn.menuName == '结束交易'}">
                {{#  if(d.orderStatus == 3){ }}
              <a class="layui-btn  layui-btn-warm layui-btn-xs" lay-event="finish">${btn.menuName}</a>
               {{#  } }}
            </c:if>
            <c:if test="${btn.menuName == '取消'}">
                {{#  if(d.orderStatus != 4){ }}
                   <a class="layui-btn layui-btn-xs  layui-btn-danger" lay-event="cancel">${btn.menuName}</a>
                {{#  } }}
            </c:if>
            <c:if test="${btn.menuName == '删除'}">
                <a class="layui-btn layui-btn-xs layui-btn-primary layui-border-black" lay-event="delete">${btn.menuName}</a>
            </c:if>
        </c:forEach>
    </script>
</div>
</div>

<div id="orderDeliveryDialog" style="padding: 30px 18px 0px 0px; display: none">
    <form class="layui-form" lay-filter="orderDeliveryFilter">
        <input
                type="text"
                name="orderNo"
               style="display: none"
        />
        <div class="layui-form-item">
            <label class="layui-form-label">物流单号</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="deliveryNo"
                        placeholder="请输入物流单号"
                        lay-verify="required"
                        autocomplete="off"
                        class="layui-input"
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">快递公司</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="company"
                        placeholder="请输入快递公司"
                        lay-verify="required"
                        autocomplete="off"
                        class="layui-input"
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">邮费</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="postage"
                        value="0"
                        placeholder="请输入邮费(默认为零)"
                        lay-verify="required"
                        autocomplete="off"
                        class="layui-input"
                />
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style order-submit-btn-style">
                <button type="button" class="layui-btn submit-btn-style" lay-submit lay-filter="order-delivery-btn" id="order-delivery-submit">立即提交</button>
                <button type="submit" class="layui-btn layui-btn-primary btn-close">关闭</button>
            </div>
        </div>
    </form>
</div>
</body>
<script>

    layui.use(['table', 'form', 'upload', 'element'], function () {


        const table = layui.table;
        const layer = layui.layer;
        const form  = layui.form;
        layui.use("form", function () {
            let form = layui.form;
            form.on("submit(orderQueryFilter)", function (data) {
                let screen = data.field;
                console.log(JSON.stringify(screen));
                table.reload('orderTable', {
                    url: '/jd/order/list?orderNo=' + screen.orderNo + '&receiverName=' + screen.receiverName + '&createTime=' + screen.createTime
                    + '&orderStatus=' + screen.orderStatus,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#orderTable",
            height: 525,
            url: '/jd/order/list', //数据接口
            parseData: function (res) {
                return {
                    "code": 0, //解析接口状态,为0才显示数据
                    "msg": res.msg,
                    "count": res.data.total,
                    "data": res.data.records
                };
            },
            page: true,
            id: "orderTable",
            cols: [
                [
                    { field: "id", title: "编号", width: 89},
                    { field: "orderNo", title: "订单编号", width: 223 },
                    { field: "createTime", title: "创建时间", width: 185, templet: "#createTimeTpl" },
                    { field: "userId", title: "用户ID", width: 170 },
                    { field: "totalPrice", title: "订单金额(元)", width: 170, templet: function (d){
                        return (d.totalPrice / 100).toFixed(2);
                        }
                    },


                    {
                        field: "paymentType", title: "支付类型", width: 156, templet: function (d) {
                            let paymentType = (d.paymentType);
                            if (paymentType == 0) {
                                return '<span 	class="layui-btn layui-btn-primary layui-border-red layui-btn-xs">线下支付</span>';
                            } else if (paymentType == 1) {
                                return '<span class="layui-btn layui-btn-primary layui-border-orange layui-btn-xs">线上支付</span>';
                            }
                        }
                    },
                    { field: "orderStatus", title: "订单状态", width: 100, templet: function (d) {
                            let state = (d.orderStatus);
                            if(state == 0){
                                return '<span 	class="layui-btn layui-btn-primary layui-border-red layui-btn-xs">用户取消</span>';
                            } else   if(state == 1){
                                return '<span class="layui-btn layui-btn-primary layui-border-orange layui-btn-xs">未付款</span>';
                            }else    if(state == 2){
                                return '<span class="layui-btn layui-btn-primary layui-border-green  layui-btn-xs">已付款</span>';
                            }
                            else  if(state == 3){
                                return '<span 	class="layui-btn layui-btn-primary layui-border-blue layui-btn-xs">已发货</span>';
                            }else   if(state == 4){
                                return '<span class="layui-btn layui-btn-xs">交易完成</span>';
                            }
                        } },


                    {
                        fixed: "right",
                        title: "操作",

                        minWidth: 138,
                        width: 240,
                        toolbar: "#orderBar",
                    },
                ],
            ],
        });
        table.on("tool(orderOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "delivery") {
                layer.open({
                    title: "编辑订单",
                    type: 1,
                    area: ["490px", "340px"],
                    content: $("#orderDeliveryDialog"),
                    success:  function (layero, index) {
                        console.log(JSON.stringify(data));
                        form.val('orderDeliveryFilter', {
                            "orderNo": data.orderNo,
                            "deliveryNo": data.deliveryNo,
                            "company": data.deliveryCompany || "",
                            "postage": data.postage,
                        });
                    },
                    end:  function (index, layero) {
                        layer.close(index);
                        form.val('orderDeliveryFilter', {
                            "orderNo": "",
                            "deliveryNo": "",
                            "company":  "",
                            "postage": "",
                        });
                        $("#orderDeliveryDialog").hide();
                    }
                });
            } else if (obj.event === "view") {
                layer.open({
                    title: "查看订单",
                    type: 2,
                    area: ["100%", "100%"],
                    content: "/jd/order/detail?orderId=" + data.id,
                    success:  function (layero, index) {
                    },

                    end:  function (index, layero) {
                        layer.close(index);
                    }

                });
            }else if (obj.event === "cancel") {
                layer.confirm("你确定要取消[" + data.orderNo + "]的订单吗？",
                    {icon:3}, function(index){
                        $.ajax({
                            url: "/jd/order/cancel",
                            data: {"orderNo": data.orderNo},
                            type: 'post',
                            dataType: 'json',
                            success: function (json) {
                                if (json.code === 200) {
                                    $(".layui-laypage-btn").click();
                                    layer.close(index);
                                    layui.layer.msg("取消订单成功", {
                                        icon: 1,

                                        time: 1200,
                                    });
                                } else {
                                    layui.layer.msg(json.msg, {
                                        icon: 1,
                                        time: 1200,
                                    });
                                }
                            }
                        });
                    });
            } else if (obj.event === "finish") {
                layer.confirm("你确定要完成[" + data.orderNo + "]的订单吗？",
                    {icon:3}, function(index){
                        $.ajax({
                            url: "/jd/order/finish",
                            data: {"orderNo": data.orderNo},
                            type: 'post',
                            dataType: 'json',
                            success: function (json) {
                                if (json.code === 200) {
                                    $(".layui-laypage-btn").click();
                                    layer.close(index);
                                    layui.layer.msg("结束交易成功", {
                                        icon: 1,

                                        time: 1200,
                                    });
                                } else {
                                    layui.layer.msg(json.msg, {
                                        icon: 1,
                                        time: 1200,
                                    });
                                }
                            }
                        });
                    });
            } else if(obj.event === "delete"){
                layer.confirm("你确定要删除编号为[" + data.orderNo + "]的订单吗？", {icon:3}
                    , function(index){
                        $.ajax({
                            url: "/jd/order/delete",
                            data: {"orderNo": data.orderNo},
                            type: 'post',
                            dataType: 'json',
                            success: function (json) {
                                if (json.code === 200) {
                                    $(".layui-laypage-btn").click();
                                    layer.close(index);
                                    layui.layer.msg("删除订单成功", {
                                        icon: 1,

                                        time: 1200,
                                    });
                                } else {
                                    layui.layer.msg(json.msg, {
                                        icon: 1,
                                        time: 1200,
                                    });
                                }
                            }
                        });
                    });
            }
        });

        form.on('submit(order-delivery-btn)', function (data) {
            $.ajax({
                url: '/jd/order/delivery',
                type: 'post',
                data: data.field,
                dataType: 'json',
                success: function (json) {
                    if (json.code === 200) {
                        layui.layer.closeAll();
                        layer.msg("编辑成功", {
                            icon: 1,
                            area: '100px',
                            time: 1500,
                            offset: 't',
                        });
                        $(".layui-laypage-btn").click();
                    } else {
                        layer.msg(json.msg, {
                            icon: 2,
                            time: 1300,
                            offset: 't',
                        });
                    }
                }
            })
            return false;
        });



        $(".btn-close").click(function (){
            layer.closeAll();
        })
    });


</script>

<script src="/jd/js/dateConversion.js"></script>
<script type="text/html" id="createTimeTpl">
    {{ dateFormat( d.createTime) }}
</script>
</html>