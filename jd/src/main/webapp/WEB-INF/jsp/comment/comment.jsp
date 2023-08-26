<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/11/22
  Time: 下午 12:13
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
    <link rel="stylesheet" href="/jd/css/comment.css"/>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <title>评论管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form">

        <div class="layui-inline">
            <label class="layui-form-label inline-name">会员名称</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="userName"
                        placeholder="请输入会员名称"
                        lay-verify="text"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label inline-name">商品名称</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="productName"
                        placeholder="请输入商品名称"
                        lay-verify="text"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline my-date-input">
            <label class="layui-form-label">评论时间</label>
            <div class="layui-input-inline">
                <input type="date" name="createTime"
                       placeholder="请输入评论时间"
                       lay-verify="text"
                       autocomplete="off"
                       class="layui-input inline-input">
            </div>
        </div>

        <div class="layui-inline  comment-status-style">
            <label class="layui-form-label inline-name">回复状态</label>
            <div class="layui-input-inline">
                <select name="status" lay-verify="">
                    <option value="-1">全部</option>
                    <option value="0">已回复</option>
                    <option value="1">未回复</option>
                </select>
            </div>
        </div>
        <div class="layui-inline query-from-style-margin-top query-btn-style">
            <div class="layui-input-block inline-input inline-style">
                <button class="layui-btn" lay-submit
                        lay-filter="commentQueryFilter">查询
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
    <c:if test="${btn.menuName == '添加虚拟评论'}">
    <button
            type="button"
            class="layui-btn layui-btn-sm layui-btn-normal create-button"
            id="create-comment">${btn.menuName}</button>
    </c:if>
</c:forEach>
</div>

<div class="base-table">
    <table id="commentTable" lay-filter="commentOpera" class="commentTable"></table>

    <script type="text/html" id="commentBar">
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

<div id="commentDialog" style="display: none; padding: 30px 10px 0px 0px">
    <form class="layui-form comment-form" lay-filter="createCommentFilter">
        <input style="display: none" type="text" name="commentId"/>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">评价内容</label>
            <div class="layui-input-block">
          <textarea
                  disabled
                  name="content"
                  lay-verify="content"
                  class="layui-textarea disable-bgcolor"
                  autocomplete="off"
          ></textarea>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">回复内容
            </label>
            <div class="layui-input-block">
          <textarea
                  name="reply"
                  lay-verify="required"
                  placeholder="请输入回复内容            "
                  class="layui-textarea"
                  autocomplete="off"
                  id="formDesc"
          ></textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">展示状态</label>
            <div class="layui-input-block form-commentstatus">
                <input type="radio" name="status" value="0" title="展示" checked="">
                <input type="radio" name="status" value="1" class="form-menustatus" title="隐藏">
            </div>
        </div>

        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style comment-submit-btn-style">
                <button type="button" class="layui-btn submit-btn-style mr20" lay-submit lay-filter="comment-edit-btn">立即提交</button>
                <button type="submit" class="layui-btn layui-btn-primary btn-close"> 关闭</button>
            </div>
        </div>
    </form>
</div>

<div id="commentCreateDialog" style="display:none; padding: 30px 10px 0px 0px">
    <form class="layui-form comment-form" lay-filter="commentCreateDialog">
        <div class="layui-form-item">
            <label class="layui-form-label">用户ID</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="userId"
                        lay-verify="required"
                        placeholder="请输入用户Id"
                        autocomplete="off"
                        class="layui-input"
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">产品ID</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="productId"
                        lay-verify="required"
                        placeholder="请输入产品ID"
                        autocomplete="off"
                        class="layui-input"
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">评分</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="score"
                        lay-verify="required"
                        placeholder="请输入评分(1-5分制)"
                        autocomplete="off"
                        class="layui-input"
                />
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">评价内容</label>
            <div class="layui-input-block">
            <textarea
                    name="content"
                    lay-verify="required"
                    class="layui-textarea"
                    autocomplete="off"
            ></textarea>
            </div>
        </div>
        <div class="layui-inline">
            <div
                    class="layui-input-block inline-input inline-style comment-submit-btn2-style"
            >
                <button
                        type="button"
                        class="layui-btn submit-btn-style mr20"
                        lay-submit
                        lay-filter="comment-add-btn"
                        id="comment-add-submit"
                >
                    立即提交
                </button>

                <button
                        type="submit"
                        class="layui-btn layui-btn-primary"
                        class="btn-close"
                >
                    关闭
                </button>
                <button
                        type="reset" style="display: none"

                        id="add-reset"
                >
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
            form.on("submit(commentQueryFilter)", function (data) {
                let screen = data.field;
                table.reload('commentTable', {
                    url: '/jd/comment/list?userName=' + screen.userName + "&productName=" + screen.productName +
                    "&createTime=" + screen.createTime + "&status=" + screen.status,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#commentTable",
            height: 480,
            url: '/jd/comment/list',
            page: true,
            parseData: function (res) {
                return {
                    "code": 0, //解析接口状态,为0才显示数据
                    "msg": res.msg,
                    "count": res.data.total,
                    "data": res.data.records
                };
            },
            id: "commentTable",
            cols: [
                [
                    {field: "id", title: "ID", width: 90},
                    {field: "userName", title: "会员名称", width: 190},
                    {field: "productName", title: "商品名称", width: 188},
                    {
                        field: "score", title: "评价", width: 90, templet: function (d) {
                            let score = (d.score);
                            if (score >= 4) {
                                return '<span  class="layui-btn  layui-btn-xs">好评</span>';
                            } else if (score >= 2) {
                                return '<span class="layui-btn  layui-btn-xs layui-btn-normal">中评</span>';
                            } else {
                                return '<span class="layui-btn layui-btn-danger layui-btn-xs">差评</span>';

                            }
                        }
                    },
                    {field: "content", title: "评价内容", width: 198},
                    {
                        field: "replyStatus", title: "回复状态", width: 120, templet: function (d) {
                            let status = (d.replyStatus);
                            if (status == 0) {
                                return '<span class="layui-btn  layui-btn-xs">已回复</span>';
                            } else {
                                return '<span class="layui-btn layui-btn-danger layui-btn-xs">未回复</span>';
                            }
                        }
                    },

                    {field: "createTime", title: "创建时间", width: 198, templet: "#createTimeTpl"},

                    {
                        field: "status", title: "状态", width: 90, templet: function (d) {
                            let status = (d.status);
                            if (status == 0) {
                                return '<span class="layui-btn layui-btn-normal layui-btn-xs">展示</span>';
                            } else {
                                return '<span class="layui-btn layui-btn-danger layui-btn-xs">隐藏</span>';
                            }
                        }
                    },

                    {field: "replyTime", title: "回复时间", width: 198, templet: "#replyTimeTpl"},

                    {
                        fixed: "right",
                        title: "操作",
                        width: 118,
                        minWidth: 118,
                        toolbar: "#commentBar",
                    },
                ],
            ],
        });


        table.on("tool(commentOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                layer.confirm("你确定要删除ID[" + data.id + "]的品牌吗？", {icon:3},
                    function(index){
                        layer.close(index);
                        $.ajax({
                            url: "/jd/comment/delete",
                            data: {"commentId": data.id},
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
                    title: "编辑评论",
                    type: 1,
                    area: ["700px", "500px"],
                    content: $("#commentDialog"),
                    success: function (layero, index) {
                        form.val('createCommentFilter', {
                            "commentId": data.id,
                            "content": data.content,
                            "status": data.status,
                            "reply": data.reply,
                        });
                    },

                    end: function (index, layero) {
                        $("#commentDialog").hide();
                        layer.close(index);


                    }

                });
            }
        });

        form.on('submit(comment-edit-btn)', function (data) {
            $.ajax({
                url: '/jd/comment/edit',
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


        $("#create-comment").click(function () {
            layer.open({
                title: "添加虚拟评论",
                type: 1,
                area: ["580px", "460px"],
                content: $("#commentCreateDialog"),
                success: function (layero, index) {
                    $("#commentCreateDialog").show();
                },
                end: function (index, layero) {
                    layer.close(index);
                    $("#add-reset").click();
                    $("#commentCreateDialog").hide();
                },
            });
        });
        form.on('submit(comment-add-btn)', function (data) {
            console.log(JSON.stringify(data.field));
            $.ajax({
                url: '/jd/comment/add',
                type: 'post',
                data: JSON.stringify(data.field),
                dataType: 'json',
                success: function (json) {
                    if (json.code === 200) {
                        layui.layer.closeAll();
                        layer.msg("添加虚拟评论成功", {
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
<script type="text/html" id="replyTimeTpl">
    {{ dateFormat( d.replyTime) }}
</script>
</html>