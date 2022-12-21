<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/11/17
  Time: 下午 01:56
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
    <link rel="stylesheet" href="/jd/css/afterSale.css" />

    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <title>售后管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form">

        <div class="layui-inline">
            <label class="layui-form-label inline-name">服务单号</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="serviceId"
                        placeholder="请输入服务单号"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline query-from-style-margin-top">
            <div class="layui-input-block inline-input inline-style">
                <button class="layui-btn" lay-submit  lay-filter="afterSaleQueryFilter">查询</button>
                <button type="reset" class="layui-btn layui-btn-primary">
                    重置
                </button>
            </div>
        </div>
    </form>
</div>

<div class="base-table">
    <table id="afterSaleTable" lay-filter="afterSaleOpera" class="afterSaleTable"></table>

    <script type="text/html" id="afterSaleBar">
        <c:forEach items="${buttons}" var="btn">
            <c:if test="${btn.menuName == '编辑'}">
                <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="edit">${btn.menuName}</a>
            </c:if>
            <c:if test="${btn.menuName == '删除'}">
                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">${btn.menuName}</a>
            </c:if>
        </c:forEach></script>

</div>
</div>

<div id="afterSaleDialog" style="display: none; padding: 18px 35px 0px 30px">

    <form class="layui-form" lay-filter="createAfterSaleFilter">
        <input type="text" name="serviceId" style="display:none;"/>

        <div class="layui-form-item">
            <label class="layui-form-label inline-name">售后状态</label>
            <div class="layui-input-inline">
                <select name="serviceStatus" lay-verify="">
                    <option value="1">已拒绝</option>
                    <option value="2">同意</option>
                     <option value="3">已完成</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block inline-input inline-style afterSale-submit-btn-style">
                <button type="button" class="layui-btn submit-btn-style mr20" lay-submit lay-filter="afterSale-edit-btn"  style="display:none;" id="afterSale-edit-submit">立即提交</button>
                <button type="submit" class="layui-btn layui-btn-primary" id="btn-close">
                    关闭
                </button>
                <button type="reset" style="display: none" id="btn-reset">
                    重置
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
            form.on("submit(afterSaleQueryFilter)", function (data) {
                let screen = data.field;
                table.reload('afterSaleTable', {
                    url: '/jd/afterSale/list?serviceId=' + screen.serviceId,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#afterSaleTable",
            height: 513,
            url: '/jd/afterSale/list', //数据接口

            parseData: function (res) { //res 即为原始返回的数
                return {
                    "code": 0, //解析接口状态,为0才显示数据
                    "msg": res.msg, //解析提示文本
                    "count": res.data.total, //解析数据长度
                    "data": res.data.records//解析数据列表
                };
            },
            page: true,
            id: "afterSaleTable",
            cols: [
                [
                    { field: "id", title: "服务单号", width: 120},
                    { field: "createTime", title: "申请时间", width: 189, templet: "#createTimeTpl"},
                    { field: "userId", title: "用户账号", width: 150},
                    { field: "orderId", title: "订单号", width: 220},
                    { field: "returnReason", title: "退款原因", width: 280},

                    { field: "serviceStatus", title: "申请状态", width: 120, templet: function (d) {
                            let state = (d.serviceStatus);
                            if(state == 0){
                                return '<span class="layui-btn layui-btn-normal layui-btn-xs">待处理</span>';
                            } else if(state == 1){
                                return '<span class="layui-btn layui-btn-danger layui-btn-xs">已拒绝</span>';
                            } else if(state == 2){
                                return '<span class="layui-btn layui-btn-normal layui-btn-xs">退货中</span>';
                            } else if(state == 3){
                                return '<span class="layui-btn layui-btn-primary layui-btn-xs">已完成</span>';
                            }
                        } },
                    { field: "processTime", title: "处理时间", width: 188 ,  templet: "#processTimeTpl"},
                    {
                        fixed: "right",
                        title: "操作",
                        width: 118,
                        minWidth: 118,
                        toolbar: "#afterSaleBar",
                    },
                ],
            ],
        });


        table.on("tool(afterSaleOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                layer.confirm("你确定要删除ID[" + data.id + "]的售后服务吗？", {icon:3},
                    function(index){
                        layer.close(index);
                        $.ajax({
                            url: "/jd/afterSale/delete",
                            data: {"serviceId": data.id},
                            type: 'post',
                            dataType: 'json',
                            success:  function (json) {
                                if (json.code === 200) {
                                    layer.msg("删除成功", {
                                        icon: 1,
                                        time: 1300,
                                        offset: 't',
                                    });
                                    $(".layui-laypage-btn").click();
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
                    title: "编辑售后服务",
                    type: 1,
                    area: ["450px", "200px"],
                    content: $("#afterSaleDialog"),
                    success:  function (layero, index) {
                        $("#afterSale-edit-submit").show();

                        form.val('createAfterSaleFilter', {
                            "serviceId": data.id,
                            "serviceStatus": data.serviceStatus,
                        });
                    },

                    end:  function (index, layero) {
                        $("#afterSaleDialog").hide();
                        layer.close(index);

                        $("#afterSale-edit-submit").hide();

                    }

                });
            }
        });

        form.on('submit(afterSale-edit-btn)', function (data) {
            console.log(JSON.stringify(data.field));
            $.ajax({
                url: '/jd/afterSale/edit',
                type: 'post',
                data: (data.field),
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
<script type="text/html" id="processTimeTpl">
    {{ dateFormat( d.processTime) }}
</script>
</html>
