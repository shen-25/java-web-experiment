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
    <link rel="stylesheet" href="/jd/css/dept.css"/>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script src="/jd/resources/sweetalert2.all.min.js"></script>
    <script src="/jd/js/dept.js"></script>
    <title>部门管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form" >

        <div class="layui-inline">
            <label class="layui-form-label inline-name">部门名称</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="deptName"
                        placeholder="请输入部门名称"
                        lay-verify="text"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>

        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style">
                <button class="layui-btn" lay-submit  lay-filter="deptQueryFilter">查询</button>
                <button type="reset" class="layui-btn layui-btn-primary">
                    重置
                </button>
            </div>
        </div>
</div>
</form>
</div>
<div class="layui-btn-container">

<c:forEach items="${buttons}" var="btn">

    <c:if test="${btn.menuName == '新增'}">
    <button
            type="button"
            class="layui-btn layui-btn-sm layui-btn-normal create-button"
            id="create-dept">
        ${btn.menuName}
    </button>
    </c:if>

</c:forEach>
</div>

<div class="base-table">
    <table id="deptTable" lay-filter="deptOpera"  class="deptTable"></table>

    <script type="text/html" id="deptBar">
<c:forEach items="${buttons}" var="btn">

    <c:if test="${btn.menuName == '编辑'}">
        <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="edit"> ${btn.menuName}</a>
   </c:if>
    <c:if test="${btn.menuName == '删除'}">
        <a
                class="layui-btn layui-btn-danger layui-btn-xs"
                lay-event="del"
        > ${btn.menuName}</a >
    </c:if>
</c:forEach>
    </script>

</div>
</div>

<div id="deptDialog" style="display: none; padding: 18px 35px 0px 30px">

    <form class="layui-form dept-form" lay-filter="createDeptFilter">
        <div class="layui-form-item" style="display: none">
            <label>部门ID</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="deptId"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">部门名称</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="deptName"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入部门名称"
                        class="layui-input form-menuName"
                />
            </div>
        </div>

        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style dept-sumbit-btn-style">
                <button class="layui-btn submit-btn-style" lay-submit lay-filter="dept-add-btn" style="display:none;" id="dept-add-submit">立即提交</button>
                <button class="layui-btn submit-btn-style" lay-submit lay-filter="dept-edit-btn"  style="display:none;" id="dept-edit-submit">立即提交</button>


                <button type="submit" class="layui-btn layui-btn-primary" id="btn-close">
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
            form.on("submit(deptQueryFilter)", function (data) {
                let screen = data.field;
                table.reload('deptTable', {
                    url: '/jd/sys/dept/list?deptName=' + screen.deptName,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#deptTable",
            height: 533,
            url: '/jd/sys/dept/list',

            parseData: function (res) {
                return {
                    "code": 0,
                    "msg": res.msg,
                    "count": res.data.total,
                    "data": res.data.records
                };
            },
            page: true,
            id: "deptTable",
            cols: [
                [
                    { field: "deptId", title: "部门ID", width: 150},
                    { field: "deptName", title: "部门名称", width: 450 },
                    { field: "createTime", title: "创建时间", width: 250, templet: "#createTimeTpl" },
                    { field: "updateTime", title: "更新时间", width: 250, templet: "#updateTimeTpl" },
                    {
                        fixed: "right",
                        title: "操作",
                        width: 118,
                        minWidth: 118,
                        toolbar: "#deptBar",
                    },
                ],
            ],
        });

        table.on("tool(deptOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                layer.confirm("你确定要删除ID[" + data.deptId + "]的部门吗？", {icon:3},
                    function(index){
                        layer.close(index);
                        $.ajax({
                            url: "/jd/sys/dept/delete",
                            data: {"deptId": data.deptId},
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
                    title: "编辑部门",
                    type: 1,
                    area: ["480px", "260px"],
                    content: $("#deptDialog"),
                    success:  function (layero, index) {
                        console.log(data.deptId);
                        $("#dept-edit-submit").show();
                        form.val('createDeptFilter', {
                            "deptName": data.deptName,
                            "deptId": data.deptId
                        });
                    },

                    end:  function (index, layero) {

                        $("#deptDialog").hide();

                        layer.close(index);

                        $("#dept-edit-submit").hide();

                    }

                });
            }
        });

        $("#create-dept").click(function(){
            layer.open({
                title: "新增部门",
                type: 1,
                area: ["480px", "260px"],
                content: $("#deptDialog"),
                success:  function (layero, index) {
                    $("#dept-add-submit").show();
                    form.val('createDeptFilter', {
                        "deptName": "",
                        "deptId": ""
                    });
                },
                end:  function (index, layero) {
                    form.render();
                    $("#deptDialog").hide();
                    layer.close(index);
                    $("#dept-add-submit").hide();

                }

            });
        });
        form.on('submit(dept-add-btn)', function (data) {
            $.ajax({
                url: "/jd/sys/dept/add",
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

        form.on('submit(dept-edit-btn)', function (data) {
            $.ajax({
                url: '/jd/sys/dept/edit',
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

