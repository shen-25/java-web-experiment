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
    <link rel="stylesheet" href="/jd/css/category.css" />

    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script src="/jd/resources/sweetalert2.all.min.js"></script>
    <script src="/jd/js/category.js"></script>
    <title>分类管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form">

        <div class="layui-inline">
            <label class="layui-form-label inline-name">分类名称</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="categoryName"
                        placeholder="请输入分类名称"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline query-from-style-margin-top">
            <div class="layui-input-block inline-input inline-style">
                <button class="layui-btn" lay-submit  lay-filter="categoryQueryFilter">查询</button>
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
    <button type="button" class="layui-btn layui-btn-sm layui-btn-normal create-button"
      id="create-category">${btn.menuName}</button>
    </c:if>
</c:forEach>
</div>



<div class="base-table">
    <table id="categoryTable" lay-filter="categoryOpera" class="categoryTable"></table>

    <script type="text/html" id="categoryBar">
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

<div id="categoryDialog" style="display: none; padding: 18px 35px 0px 30px">

    <form class="layui-form category-form" lay-filter="createCategoryFilter">
        <div class="layui-form-item" style="display: none">
            <label>分类ID</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="categoryId"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">分类名称</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="categoryName"
                        lay-verify="categoryName"
                        autocomplete="off"
                        placeholder="请输入分类名称"
                        class="layui-input form-categoryName"
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">分类排序</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="categoryOrder"
                        lay-verify="categoryOrder"
                        autocomplete="off"
                        placeholder="请输入分类排序(值越小排序越前)"
                        class="layui-input "
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">分类状态</label>
            <div class="layui-input-block form-categoryState">
                <input type="radio" name="categoryState" value="0" title="正常" checked="">
                <input type="radio" name="categoryState" value="1"  class="form-menuState" title="停用">
            </div>
        </div>

        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style category-submit-btn-style">
                <button  type="button" class="layui-btn submit-btn-style mr20" lay-submit lay-filter="category-add-btn" style="display:none;" id="category-add-submit">立即提交</button>
                <button type="button" class="layui-btn submit-btn-style mr20" lay-submit lay-filter="category-edit-btn"  style="display:none;" id="category-edit-submit">立即提交</button>
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
            form.on("submit(categoryQueryFilter)", function (data) {
                let screen = data.field;
                table.reload('categoryTable', {
                    url: '/jd/store/category/list?categoryName=' + screen.categoryName,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#categoryTable",
            height: 513,
            url: '/jd/store/category/list', //数据接口

            parseData: function (res) { //res 即为原始返回的数
                return {
                    "code": 0, //解析接口状态,为0才显示数据
                    "msg": res.msg, //解析提示文本
                    "count": res.data.total, //解析数据长度
                    "data": res.data.records//解析数据列表
                };
            },
            page: true,
            id: "categoryTable",
            cols: [
                [
                    { field: "categoryId", title: "分类ID", width: 100},
                    { field: "categoryName", title: "分类名称", width: 390 },

                    { field: "categoryState", title: "状态", width: 120, templet: function (d) {
                            let state = (d.categoryState);
                            if(state == 0){
                                return '<span class="layui-btn layui-btn-normal layui-btn-xs">正常</span>';
                            } else{
                                return '<span class="layui-btn layui-btn-danger layui-btn-xs">停用</span>';
                            }
                        } },
                    { field: "categoryOrder", title: "分类排序", width: 160},
                    { field: "createTime", title: "创建时间", width: 198 ,  templet: "#createTimeTpl"},
                    { field: "updateTime", title: "更新时间", width: 198 ,  templet: "#updateTimeTpl"},
                    {
                        fixed: "right",
                        title: "操作",
                        width: 118,
                        minWidth: 118,
                        toolbar: "#categoryBar",
                    },
                ],
            ],
        });


        table.on("tool(categoryOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                layer.confirm("你确定要删除ID[" + data.categoryId + "]的商品分类吗？", {icon:3},
                    function(index){
                        layer.close(index);
                        $.ajax({
                            url: "/jd/store/category/delete",
                            data: {"categoryId": data.categoryId},
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
                    title: "编辑分类",
                    type: 1,
                    area: ["580px", "370px"],
                    content: $("#categoryDialog"),
                    success:  function (layero, index) {
                        console.log(data.categoryState);
                        $("#category-add-submit").hide();
                        $("#category-edit-submit").show();
                        $("#img-show").attr("src", data.categoryImage);
                        console.log(data.categoryId)

                        form.val('createCategoryFilter', {
                            "categoryId": data.categoryId,
                            "categoryName": data.categoryName,

                            "categoryState": data.categoryState,

                            "createTime": data.createTime,
                            "updateTime": data.updateTime,


                            "categoryOrder": data.categoryOrder,
                        });
                    },

                    end:  function (index, layero) {
                        $("#categoryDialog").hide();
                        layer.close(index);

                        $("#category-edit-submit").hide();

                    }

                });
            }
        });

        $("#create-category").click(function(){
            layer.open({
                title: "新增分类",
                type: 1,
                area: ["580px", "370px"],
                content: $("#categoryDialog"),
                success:  function (layero, index) {
                    $("#btn-reset").click();
                    $("#category-edit-submit").hide();

                    $("#category-add-submit").show();


                },
                end:  function (index, layero) {
                    $("#categoryDialog").hide();
                    layer.close(index);

                    $("#category-add-submit").hide();
                }

            });
        });
        form.on('submit(category-add-btn)', function (data) {
            console.log(JSON.stringify(data.field));
            $.ajax({
                url: "/jd/store/category/add",
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

        form.on('submit(category-edit-btn)', function (data) {
            console.log(JSON.stringify(data.field));
            $.ajax({
                url: '/jd/store/category/edit',
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
