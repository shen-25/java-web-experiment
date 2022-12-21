<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/11/15
  Time: 下午 08:25
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
    <link rel="stylesheet" href="/jd/css/brand.css"/>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script src="/jd/resources/sweetalert2.all.min.js"></script>
    <title>品牌管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form">

        <div class="layui-inline">
            <label class="layui-form-label inline-name">品牌ID</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="brandId"
                        placeholder="请输入品牌ID"
                        lay-verify="text"

                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label inline-name">品牌名称</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="brandName"
                        placeholder="请输入品牌名称"
                        lay-verify="text"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>

        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style">
                <button class="layui-btn" lay-submit
                        lay-filter="brandQueryFilter">查询</button>
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
                id="create-brand">
            ${btn.menuName}
        </button>
    </c:if>
</c:forEach>    </div>


<div class="base-table">
    <table id="brandTable" lay-filter="brandOpera" class="brandTable"></table>

    <script type="text/html" id="brandBar">
    <c:forEach items="${buttons}" var="btn">
        <c:if test="${btn.menuName == '编辑'}">
            <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="edit">${btn.menuName}</a>
        </c:if>

        <c:if test="${btn.menuName == '删除'}">
        <a
                    class="layui-btn layui-btn-danger layui-btn-xs"
                    lay-event="del">${btn.menuName}</a>
        </c:if>
                </c:forEach>
        </script>

</div>
</div>

<div id="brandDialog" style="display: none; padding: 18px 35px 49px 30px">

    <form class="layui-form brand-form" lay-filter="createBrandFilter">
        <div class="layui-form-item" style="display: none">
            <label >菜单ID</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="brandId"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">品牌名称</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="brandName"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入品牌名称"
                        class="layui-input form-brandName"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">品牌LOGO</label>
            <button type="button" class="layui-btn" id="upload-img-btn">
                <i class="layui-icon"></i>点击上传
            </button>
            <input id="brandPicUrl" name="brandPicUrl" style="display: none"; >
            <div class="layui-upload" style="margin-left: 110px">
                <div class="layui-upload-list div-upload-img-parent">
                    <img class="layui-upload-img" id="img-show"/>
                </div>
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">品牌描述</label>
            <div class="layui-input-block">
            <textarea
                    name="brandDesc"

                    placeholder="请输入品牌描述"
                    class="layui-textarea"
                    autocomplete="off"
                    id="formDesc"
            ></textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">排序</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="brandOrder"
                        lay-verify="title"
                        autocomplete="off"
                        placeholder="请输入排序"
                        class="layui-input form-brandOrder"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">品牌状态</label>
            <div class="layui-input-block form-brandState">
                <input
                        type="radio"
                        name="brandState"
                        value="0"
                        title="正常"
                        checked="true"
                />
                <input
                        type="radio"
                        name="brandState"
                        value="1"
                        class="form-brandState"
                        title="停用"
                />
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style brand-sumbit-btn-style">
                <button  type="button" class="layui-btn sumbit-btn-style mr20" lay-submit lay-filter="brand-add-btn" style="display:none;" id="brand-add-submit">立即提交</button>
                <button type="button" class="layui-btn sumbit-btn-style mr20" lay-submit lay-filter="brand-edit-btn"  style="display:none;" id="brand-edit-submit">立即提交</button>
                <button type="submit" class="layui-btn layui-btn-primary" id="btn-close">
                    关闭
                </button>
                <button type="reset" id="btn-reset" style="display: none">
                    重置
                </button>
            </div>
        </div>
    </form>
</div>
</body>
<script>

    layui.use(['table', 'form', 'upload', 'element'], function () {


        const table = layui.table;
        const layer = layui.layer;
        const form = layui.form;
        const upload = layui.upload;
        const element = layui.element;
        layui.use("form", function () {
            let form = layui.form;
            form.on("submit(brandQueryFilter)", function (data) {
                let screen = data.field;
                table.reload('brandTable', {
                    url: '/jd/store/brand/list?brandId=' + screen.brandId + '&brandName=' + screen.brandName,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#brandTable",
            height: 533,
            url: '/jd/store/brand/list', //数据接口
            page: true,
            parseData: function (res) { //res 即为原始返回的数
                return {
                    "code": 0, //解析接口状态,为0才显示数据
                    "msg": res.msg, //解析提示文本
                    "count": res.data.total, //解析数据长度
                    "data": res.data.records//解析数据列表
                };
            },
            id: "brandTable",
            cols: [
                [
                    {field: "brandId", title: "品牌ID", width: 85},
                    {field: "brandName", title: "名称", width: 160},
                    {
                        field: "brandPicUrl", title: "LOGO", width: 150, templet: function (d) {
                          let brandPicUrl = String(d.brandPicUrl);
              return "<a href=" + brandPicUrl + " target='_blank'><img src=" + brandPicUrl + " height='26'></a></div>";
                        }
                    },
                    {
                        field: "brandState", title: "状态", width: 75, templet: function (d) {
                            let state = (d.brandState);
                            if (state == 0) {
                                return '<span class="layui-btn layui-btn-normal layui-btn-xs">正常</span>';
                            } else {
                                return '<span class="layui-btn layui-btn-danger layui-btn-xs">停用</span>';
                            }
                        }
                    },
                    {field: "brandDesc", title: "描述", width: 320},
                    {field: "brandOrder", title: "排序", width: 90},
                    {field: "createTime", title: "创建时间", width: 178,  templet: "#createTimeTpl"},
                    {field: "updateTime", title: "更新时间", width: 178,  templet: "#updateTimeTpl"},
                    {
                        fixed: "right",
                        title: "操作",
                        width: 118,
                        minWidth: 118,
                        toolbar: "#brandBar",
                    },
                ],
            ],
        });

        var uploadInst = upload.render({
            elem: '#upload-img-btn'
            , url: '/jd/store/brand/add/img-upload'
            ,before: function(obj){
                obj.preview(function(index, file, result){
                    $('#img-show').attr('src', result);
                });

                element.progress('demo', '0%');
                layer.msg('上传中', {icon: 16, time: 0});
            }
            ,done: function(res){

                if(res.code > 0){
                    return layer.msg('上传失败');
                }

                $("#brandPicUrl").val(res.msg);
            }

            ,progress: function(n, elem, e){
                element.progress('demo', n + '%');
                if(n == 100){
                    layer.msg('上传完毕', {icon: 1});
                }
            }
        });

        table.on("tool(brandOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                layer.confirm("你确定要删除ID[" + data.brandId + "]的品牌吗？", {icon:3},
                    function(index){
                        layer.close(index);
                        $.ajax({
                            url: "/jd/store/brand/delete",
                            data: {"brandId": data.brandId},
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
                    title: "编辑品牌",
                    type: 1,
                    area: ["100%", "100%"],
                    content: $("#brandDialog"),
                    success: function (layero, index) {
                        $("#brand-edit-submit").show();
                        $("#brand-add-submit").hide();
                        $("#img-show").attr("src", data.brandPicUrl);
                        form.val('createBrandFilter', {
                            "brandName": data.brandName,
                            "brandPicUrl": data.brandPicUrl,
                            "brandState": data.brandState,
                            "brandDesc": data.brandDesc,
                            "brandOrder": data.brandOrder,
                            "brandId": data.brandId,
                        });
                    },

                    end: function (index, layero) {
                        $("#brandDialog").hide();
                        layer.close(index);
                        form.render();
                        $("#brand-edit-submit").hide();

                    }

                });
            }
        });

        $("#create-brand").click(function () {
            layer.open({
                title: "新增品牌",
                type: 1,
                area: ["100%", "100%"],
                content: $("#brandDialog"),
                success: function (layero, index) {
                    $("#btn-reset").click();
                    $("#brand-add-submit").show();
                    $("#brand-edit-submit").hide();
                    $("#img-show").removeAttr("src");

                },
                end: function (index, layero) {
                    $("#brandDialog").hide();
                    layer.close(index);
                    form.render();
                    $("#brand-add-submit").hide();

                }

            });
        });
        form.on('submit(brand-add-btn)', function (data) {
            const dataForm =  data.field;
            delete dataForm.file;
            console.log(JSON.stringify(dataForm));
            $.ajax({
                url: "/jd/store/brand/add",
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

        form.on('submit(brand-edit-btn)', function (data) {
            const dataForm =  data.field;
            delete dataForm.file;
            console.log(JSON.stringify(dataForm));
            $.ajax({
                url: "/jd/store/brand/edit",
                type: 'post',
                data: JSON.stringify(dataForm),
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
        $("#btn-close").click(function () {
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

