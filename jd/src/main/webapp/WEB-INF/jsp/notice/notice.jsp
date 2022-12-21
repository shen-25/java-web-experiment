<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/11/23
  Time: 上午 11:27
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
    <link rel="stylesheet" href="/jd/css/notice.css"/>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <title>公告管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form">

        <div class="layui-inline">
            <label class="layui-form-label inline-name">公告标题</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="title"
                        placeholder="请输入公告标题"

                        lay-verify="text"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label inline-name">公告内容</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="content"
                        placeholder="请输入公告内容"


                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>

        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style">
                <button class="layui-btn" lay-submit
                        lay-filter="noticeQueryFilter">查询
                </button>
                <button type="reset" class="layui-btn layui-btn-primary">
                    重置
                </button>
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
            id="create-notice"
    >
            ${btn.menuName}
    </button>
</c:if>
</c:forEach>
</div>

<div class="base-table">
    <table id="noticeTable" lay-filter="noticeOpera" class="noticeTable"></table>


    <script type="text/html" id="noticeBar">
        <c:forEach items="${buttons}" var="btn">

        <c:if test="${btn.menuName == '编辑'}">
        <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="edit"> ${btn.menuName}</a>
        </c:if>
        <c:if test="${btn.menuName == '删除'}">
        <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"> ${btn.menuName}</a>
        </c:if>
        </c:forEach>
    </script>

</div>
</div>

<div id="noticeDialog" style="display: none; padding: 30px 20px 10px 0px">
    <form class="layui-form notice-form" lay-filter="createNoticeFilter">
        <div class="layui-form-item ">
            <label class="layui-form-label inline-name">公告标题</label>
            <div class="layui-input-block ">
                <input
                        type="text"
                        name="title"
                        placeholder="请输入公告标题"
                        lay-verify="required"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <input style="display: none" type="text" name="id"/>

        <div class="layui-form-item ">
            <label class="layui-form-label">公告内容</label>
            <div class="layui-input-block">
            <textarea
                    name="content"
                    lay-verify="required"
                    placeholder="请输入公告内容"
                    class="layui-textarea"
                    autocomplete="off"></textarea>
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style notice-submit-btn-style">
                <button type="button" class="layui-btn submit-btn-style mr20" style="display: none" lay-submit lay-filter="notice-add-btn" id="notice-add-btn">立即提交</button>
                <button type="button" class="layui-btn submit-btn-style mr20"   style="display: none" lay-submit lay-filter="notice-edit-btn" id="notice-edit-btn">立即提交</button>
                <button type="submit" class="layui-btn layui-btn-primary btn-close"> 关闭</button>
                <button type="reset" id="reset-btn"style="display: none">重置</button>
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
            form.on("submit(noticeQueryFilter)", function (data) {
                let screen = data.field;

                table.reload('noticeTable', {
                    url: '/jd/notice/list?title=' + screen.title + "&content=" + screen.content,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#noticeTable",
            height: 520,
            url: '/jd/notice/list',
            page: true,
            parseData: function (res) {
                return {
                    "code": 0, //解析接口状态,为0才显示数据
                    "msg": res.msg,
                    "count": res.data.total,
                    "data": res.data.records
                };
            },
            id: "noticeTable",
            cols: [
                [
                    {field: "id", title: "ID", width: 90},
                    {field: "title", title: "公告标题", width: 200},
                    {field: "content", title: "内容", width: 505},

                    {field: "createTime", title: "创建时间", width: 188, templet: "#createTimeTpl"},

                    {field: "updateTime", title: "更新时间", width: 188, templet: "#updateTimeTpl"},

                    {
                        fixed: "right",
                        title: "操作",
                        width: 118,
                        minWidth: 118,
                        toolbar: "#noticeBar",
                    },
                ],
            ],
        });


        table.on("tool(noticeOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                layer.confirm("你确定要删除ID[" + data.id + "]的公告吗？", {icon:3},
                    function(index){
                        layer.close(index);
                        $.ajax({
                            url: "/jd/notice/delete",
                            data: {"noticeId": data.id},
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
                    title: "编辑公告",
                    type: 1,
                    area: ["580px", "380px"],
                    content: $("#noticeDialog"),
                    success: function (layero, index) {
                        $("#notice-add-btn").hide();
                        $("#notice-edit-btn").show();
                        form.val('createNoticeFilter', {
                            "id": data.id,
                            "content": data.content,
                            "title": data.title
                        });
                    },

                    end: function (index, layero) {
                        $("#noticeDialog").hide();
                        layer.close(index);
                        $("#notice-edit-btn").hide();

                    }

                });
            }
        });

        form.on('submit(notice-edit-btn)', function (data) {
            $.ajax({
                url: '/jd/notice/edit',
                type: 'post',
                data: JSON.stringify(data.field),
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


        $("#create-notice").click(function () {
            layer.open({
                title: "创建公告",
                type: 1,
                area: ["580px", "380px"],
                content: $("#noticeDialog"),
                success: function (layero, index) {
                    $("#reset-btn").click();
                    $("#notice-add-btn").show();
                    $("#notice-edit-btn").hide();
                },
                end: function (index, layero) {
                    layer.close(index);
                    $("#noticeDialog").hide();
                    $("#add-reset").click();
                    $("#notice-add-btn").hide();

                },
            });
        });
        form.on('submit(notice-add-btn)', function (data) {
            $.ajax({
                url: '/jd/notice/add',
                type: 'post',
                data: JSON.stringify(data.field),
                dataType: 'json',
                success: function (json) {
                    if (json.code === 200) {
                        layui.layer.closeAll();
                        layer.msg("创建公告成功", {
                            icon: 1,
                            time: 1500,
                            offset: 't',
                        });
                        $("#add-reset").click();
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

        $(".btn-close").click(function () {
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