<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/11/18
  Time: 上午 10:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="/jd/resources/layui/css/layui.css"/>
    <link rel="stylesheet" href="/jd/css/index.css"/>
    <link rel="stylesheet" href="/jd/css/cart.css"/>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script src="/jd/resources/sweetalert2.all.min.js"></script>
    <title>购物车管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form" >

        <div class="layui-inline">
            <label class="layui-form-label inline-name">用户ID</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="userId"
                        placeholder="请输入用户ID"
                        lay-verify="text"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>

        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style">
                <button class="layui-btn" lay-submit  lay-filter="cartQueryFilter">查询</button>
                <button type="reset" class="layui-btn layui-btn-primary">
                    重置
                </button>
            </div>
        </div>
</form>
</div>

<div class="layui-btn-container">
    <c:forEach items="${buttons}" var="btn">

    <c:if test="${btn.menuName == '新增虚拟购物车'}">
    <button type="button" class="layui-btn layui-btn-sm layui-btn-normal only-btn-container" id="create-cart">
         ${btn.menuName}
    </button>
    </c:if>
    <c:if test="${btn.menuName == '结算虚拟购物车'}">
    <button type="button" class="layui-btn layui-btn-sm  only-btn-container" id="order-cart-pay">
        ${btn.menuName}
    </button>
    </c:if>

</c:forEach>
</div>

<div class="base-table">
    <table id="cartTable" lay-filter="cartOpera"  class="cartTable"></table>
    <script type="text/html" id="cartBar">
    <c:forEach items="${buttons}" var="btn">
         <c:if test="${btn.menuName == '编辑'}">
        <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="edit">${btn.menuName}</a>
        </c:if>
            <c:if test="${btn.menuName == '删除'}">
            <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">${btn.menuName}</a>
            </c:if>
        </c:forEach>
    </script>

</div>
</div>

<div id="cartDialog" style="display: none; padding: 18px 35px 0px 30px">

    <form class="layui-form cart-form" lay-filter="createCartFilter">
        <div class="layui-form-item">
            <label class="layui-form-label">用户ID</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="userId"
                        id="userId"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入用户ID"
                        class="layui-input "
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">商品ID</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="productId"
                        id="productId"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入商品ID"
                        class="layui-input "
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">数量</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="quantity"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入商品数量"
                        class="layui-input "
                />
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style cart-sumbit-btn-style">
                <button class="layui-btn submit-btn-style" lay-submit lay-filter="cart-add-btn" style="display:none;" id="cart-add-submit">立即提交</button>
                <button class="layui-btn submit-btn-style" lay-submit lay-filter="cart-edit-btn"  style="display:none;" id="cart-edit-submit">立即提交</button>


                <button type="submit" class="layui-btn layui-btn-primary" id="btn-close">
                    关闭
                </button>
            </div>
        </div>
    </form>
</div>

<div id="cartOrderDialog" style="display: none; padding: 18px 35px 0px 30px">

    <form class="layui-form cart-form" lay-filter="cartOrderFilter">
        <div class="layui-form-item">
            <label class="layui-form-label">用户ID</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="userId"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入用户ID"
                        class="layui-input "
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">收件人名字</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="receiverName"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入收件人名字"
                        class="layui-input "
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">手机号</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="receiverMobile"
                        lay-verify="required | phone"
                        autocomplete="off"
                        placeholder="请输入收件人手机号"
                        class="layui-input "
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">收件人地址</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="receiverAddress"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入收件人地址"
                        class="layui-input "
                />
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style cart-sumbit-btn-style">
                <button class="layui-btn submit-btn-style" lay-submit lay-filter="cart-order-pay-btn" id="cart-order-pay-submit">立即提交</button>
                <button type="submit" class="layui-btn layui-btn-primary btn-close">
                    关闭
                </button>
            </div>
        </div>
    </form>
</div>
</body>
<script>

    layui.use(['table', 'form'], function () {

        const table = layui.table;
        const layer = layui.layer;
        const form  = layui.form;
        layui.use("form", function () {
            let form = layui.form;
            form.on("submit(cartQueryFilter)", function (data) {
                let screen = data.field;
                table.reload('cartTable', {
                    url: '/jd/cart/list?userId=' + screen.userId,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#cartTable",
            height: 533,
            url: '/jd/cart/list',

            parseData: function (res) {
                return {
                    "code": 0,
                    "msg": res.msg,
                    "count": res.data.total,
                    "data": res.data.records
                };
            },
            page: true,
            id: "cartTable",
            cols: [
                [
                    { field: "id", title: "购物车ID", width: 150},
                    { field: "userId", title: "用户ID", width: 150 },

                    { field: "productId", title: "商品ID", width: 150 },
                    { field: "quantity", title: "数量", width: 150 },
                    {
                        field: "selected", title: "状态", width: 95, templet: function (d) {
                            let selected = (d.selected);
                            if (selected == 0) {
                                return '<span class="layui-btn layui-btn-danger layui-btn-xs">未选中</span>';
                            } else if (selected == 1) {
                                return '<span class="layui-btn  layui-btn-xs">已选中</span>';
                            }
                        }
                    },
                    { field: "createTime", title: "创建时间", width: 250, templet: "#createTimeTpl" },
                    { field: "updateTime", title: "更新时间", width: 250, templet: "#updateTimeTpl" },
                    {
                        fixed: "right",
                        title: "操作",
                        width: 118,
                        minWidth: 118,
                        toolbar: "#cartBar",
                    },
                ],
            ],
        });

        table.on("tool(cartOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                layer.confirm("你确定要删除ID[" + data.id + "]的购物车吗？", {icon:3},
                    function(index){
                        layer.close(index);
                        $.ajax({
                            url: "/jd/cart/delete",
                            data: {"cartId": data.id},
                            type: 'post',
                            dataType: 'json',
                            success:  function (json) {
                                if (json.code === 200) {
                                    $(".layui-laypage-btn").click();
                                    layer.msg("删除成功", {
                                        icon: 1,
                                        time: 1300,
                                        offset: 't',
                                    })
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
            } else if (obj.event === "edit") {
                layer.open({
                    title: "编辑购物车",
                    type: 1,
                    area: ["480px", "350px"],
                    content: $("#cartDialog"),
                    success:  function (layero, index) {

                        $("#cart-edit-submit").show();
                        $("#userId").addClass("disable-bgcolor");
                        $("#userId").attr("disabled", true);
                        $("#productId").addClass("disable-bgcolor");
                        $("#productId").attr("disabled", true);

                        form.val('createCartFilter', {
                            "productId": data.productId,
                            "userId": data.userId,
                            "quantity": data.quantity,
                            "cartId": data.cartId
                        });
                    },

                    end:  function (index, layero) {

                        $("#cartDialog").hide();

                        layer.close(index);

                        $("#cart-edit-submit").hide();

                    }

                });
            }
        });

        $("#create-cart").click(function(){
            layer.open({
                title: "新增虚拟购物车",
                type: 1,
                area: ["480px", "350px"],
                content: $("#cartDialog"),
                success:  function (layero, index) {
                    $("#cart-add-submit").show();
                    $("#userId").removeClass("disable-bgcolor");
                    $("#productId").removeClass("disable-bgcolor");
                    $("#userId").attr("disabled", false);
                    $("#productId").attr("disabled", false);
                    form.val('createCartFilter', {
                        "quantity": "",
                        "productId": "",
                        "userId": "",
                        "cartId": ""
                    });
                },
                end:  function (index, layero) {
                    form.render();
                    $("#cartDialog").hide();
                    layer.close(index);
                    $("#cart-add-submit").hide();

                }

            });
        });
        form.on('submit(cart-add-btn)', function (data) {
            console.log(JSON.stringify(data.field));
            $.ajax({
                url: "/jd/cart/add",
                type: 'post',
                data: JSON.stringify(data.field),
                contentType: 'application/json',
                dataType: 'json',
                success: function (json) {
                    if (json.code === 200) {
                        layui.layer.closeAll();
                        layer.msg("新增成功", {
                            icon: 1,
                            time: 1300,
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
            });
            return false;
        });

        $("#order-cart-pay").click(function(){
            layer.open({
                title: "结算虚拟购物车",
                type: 1,
                area: ["520px", "400px"],
                content: $("#cartOrderDialog"),
                success:  function (layero, index) {
                },
                end:  function (index, layero) {
                    $("#cartOrderDialog").hide();
                    layer.close(index);

                }

            });
        });
        form.on('submit(cart-order-pay-btn)', function (data) {
            console.log(JSON.stringify(data.field));
            $.ajax({
                url: "/jd/order/cart/pay",
                type: 'post',
                data: JSON.stringify(data.field),
                contentType: 'application/json',
                dataType: 'json',
                success: function (json) {
                    if (json.code === 200) {
                        layui.layer.closeAll();
                        layer.msg("结算虚拟购物车成功", {
                            icon: 1,
                            time: 1300,
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
            });
            return false;
        });

        form.on('submit(cart-edit-btn)', function (data) {
            $.ajax({
                url: '/jd/cart/edit',
                type: 'post',
                data: JSON.stringify(data.field),
                contentType: 'application/json',
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
        $("#btn-close").click(function(){
            layer.closeAll();

        });
    });
</script>
<script src="/jd/js/dateConversion.js"></script>
<script type="text/html" id="createTimeTpl">
    {{ dateFormat( d.createTime) }}
</script>
<script type="text/html" id="updateTimeTpl">
    {{ dateFormat( d.updateTime) }}
</script>

</html>

