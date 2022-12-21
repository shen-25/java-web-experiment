<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/11/27
  Time: 下午 01:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="/jd/resources/layui/css/layui.css" />
    <link rel="stylesheet" href="/jd/css/index.css" />
    <link rel="stylesheet" href="/jd/css/advert.css" />
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/js/dateConversion.js"></script>

    <title>广告管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form">

        <div class="layui-inline">
            <label class="layui-form-label inline-name">广告名称</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="advertName"
                        placeholder="请输入广告名称"
                        lay-verify="text"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-block  inline-style">
                <button class="layui-btn" lay-submit
                        lay-filter="advertQueryFilter">查询</button>
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
            id="create-advert">
        ${btn.menuName}
    </button>
    </c:if>
</c:forEach>
    </div>

<div class="base-table">
    <table id="advertTable" lay-filter="advertOpera" class="advertTable"></table>

    <script type="text/html" id="advertBar">
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

<div id="advertDialog" style="display: none; padding: 18px 35px 60px 30px">

    <form class="layui-form advert-form" lay-filter="createAdvertFilter">
        <div style="display: none">
            <label>ID</label>
            <input
                    type="text"
                    name="id"
            />
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">广告名称</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="name"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入广告名称"
                        class="layui-input "
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">开始时间</label>
            <div class="layui-input-inline">
                <input type="date" name="startTime"
                       placeholder="请选择开始时间"
                       lay-verify="required"
                       autocomplete="off"
                       class="layui-input inline-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">结束时间</label>
            <div class="layui-input-inline">
                <input type="date" name="endTime"
                       placeholder="请选择开始时间"
                       lay-verify="required"
                       autocomplete="off"
                       class="layui-input inline-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">广告状态</label>
            <div class="layui-input-block">
                <input
                        type="radio"
                        name="status"
                        value="0"
                        title="上线"
                        checked=""
                />
                <input
                        type="radio"
                        name="status"
                        value="1"
                        title="下线"
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">广告图片</label>
            <input id="picUrl" name="picUrl" style="display: none" ;>
            <button type="button" class="layui-btn" id="upload-img-btn">
                <i class="layui-icon"></i>点击上传
            </button>
            <div class="layui-upload" style="margin-left: 110px">
                <div class="layui-upload-list div-upload-img-parent">
                    <img class="layui-upload-img" id="img-show"/>
                </div>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">广告链接</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="url"
                        lay-verify="title"
                        autocomplete="off"
                        placeholder="请输入广告链接"
                        class="layui-input"
                />
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">广告描述</label>
            <div class="layui-input-block">
            <textarea
                    name="advertDesc"
                    placeholder="请输入广告描述"
                    class="layui-textarea"
                    autocomplete="off"
                    id="formDesc"
            ></textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">广告排序</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="advertOrder"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入广告排序(值越小排序越前)"
                        class="layui-input "
                />
            </div>
        </div>



        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style advert-submit-btn-style">
                <button  type="button" class="layui-btn submit-btn-style mr20" lay-submit lay-filter="advert-add-btn" style="display:none;" id="advert-add-submit">立即提交</button>
                <button type="button" class="layui-btn submit-btn-style mr20" lay-submit lay-filter="advert-edit-btn"  style="display:none;" id="advert-edit-submit">立即提交</button>
                <button type="submit" class="layui-btn layui-btn-primary" id="btn-close">
                    关闭
                </button>
                <button type="reset"  id="btn-reset" style="display: none">
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
        const form  = layui.form;
        const upload = layui.upload;
        const element = layui.element;

        layui.use("form", function () {
            let form = layui.form;
            form.on("submit(advertQueryFilter)", function (data) {
                let screen = data.field;
                console.log(JSON.stringify(screen));
                table.reload('advertTable', {
                    url: '/jd/advert/list?advertName=' + screen.advertName,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#advertTable",
            height: 523,
            url: '/jd/advert/list',
            parseData: function (res) {
                return {
                    "code": 0,
                    "msg": res.msg,
                    "count": res.data.total,
                    "data": res.data.records
                };
            },
            page: true,
            id: "advertTable",
            cols: [
                [
                    {field: "id", title: "ID", width: 75},
                    {field: "name", title: "广告名称", width: 170},
                    {
                        field: "picUrl", title: "图片", width: 100, templet: function (d) {
                            let picUrl = String(d.picUrl);
                            return "<a href=" + picUrl + " target='_blank'><img src=" + picUrl + " height='26'></a></div>";
                        }
                    },

                    {field: "startTime", title: "开始时间", width: 188,  templet: "#startTimeTpl"},
                    {field: "endTime", title: "结束时间", width: 188,  templet: "#endTimeTpl"},
                    {
                        field: "status", title: "状态", width: 80, templet: function (d) {
                            let state = (d.status);
                            if (state == 0) {
                                return '<span class="layui-btn layui-btn-normal layui-btn-xs">上线</span>';
                            } else if(state == 1){
                                return '<span class="layui-btn layui-btn-danger layui-btn-xs">下线</span>';
                            }
                        }
                    },
                    {field: "url", title: "广告链接", width: 170},
                    {field: "advertDesc", title: "描述", width: 200},
                    {field: "advertOrder", title: "排序", width: 100},
                    {field: "createTime", title: "创建时间", width: 188,  templet: "#createTimeTpl"},
                    {field: "updateTime", title: "更新时间", width: 188,  templet: "#updateTimeTpl"},
                    {
                        fixed: "right",
                        title: "操作",
                        width: 118,
                        toolbar: "#advertBar",
                    },
                ],
            ],
        });

        let uploadInst = upload.render({
            elem: '#upload-img-btn'
            , url: '/jd/advert/upload/image'
            , before: function (obj) {
                obj.preview(function (index, file, result) {
                    $('#img-show').attr('src', result);
                });

                element.progress('demo', '0%');
                layer.msg('上传中', {icon: 16, time: 0});
            }
            , done: function (res) {
                if (res.code > 0) {
                    return layer.msg('上传失败');
                }
                $("#picUrl").val(res.msg);

            }
            //进度条
            , progress: function (n, elem, e) {
                element.progress('demo', n + '%');
                if (n == 100) {
                    layer.msg('上传完毕', {icon: 1});
                }
            }
        });

        table.on("tool(advertOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                layer.confirm("你确定要删除ID[" + data.id + "]的广告吗？", {icon: 3},
                    function (index) {
                        layer.close(index);
                        $.ajax({
                            url: "/jd/advert/delete",
                            data: {"advertId": data.id},
                            type: 'post',
                            dataType: 'json',
                            success: function (json) {
                                if (json.code === 200) {
                                    $(".layui-laypage-btn").click();
                                    layer.msg("删除广告成功", {
                                        icon: 1,
                                        time: 1300,
                                        offset: 't',
                                    })
                                } else {
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
            }else if (obj.event === "edit") {
                layer.open({
                    title: "编辑商品",
                    type: 1,
                    area: ["100%", "100%"],
                    content: $("#advertDialog"),
                    success:  function (layero, index) {
                        $("#advert-edit-submit").show();
                        $("#advert-add-submit").hide();
                        $("#img-show").attr("src", data.picUrl);
                        form.val('createAdvertFilter', {
                           "id": data.id,
                            "name": data.name,
                            "picUrl": data.picUrl,
                            "startTime": timestampToTime(new Date(data.startTime).getTime()),
                            "endTime": timestampToTime(new Date(data.endTime).getTime()),
                            "status": data.status,
                            "url": data.url,
                            "advertDesc": data.advertDesc,
                            "advertOrder": data.advertOrder

                        });
                    },

                    end:  function (index, layero) {
                        $("#advertDialog").hide();
                        layer.close(index);
                        form.render();
                        $("#advert-edit-submit").hide();

                    }

                });
            }
        });

        $("#create-advert").click(function(){
            layer.open({
                title: "新增广告",
                type: 1,
                area: ["100%", "100%"],
                content: $("#advertDialog"),
                success:  function (layero, index) {
                    $("#btn-reset").click();
                    $('#img-show').removeAttr('src');
                    $("#advert-edit-submit").hide();
                    $("#advert-add-submit").show();

                },
                end:  function (index, layero) {
                    $("#advertDialog").hide();
                    layer.close(index);
                    form.render();
                    $("#advert-add-submit").hide();

                }

            });
        });
        form.on('submit(advert-add-btn)', function (data) {
            $.ajax({
                url: "/jd/advert/add",
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

        form.on('submit(advert-edit-btn)', function (data) {
            $.ajax({
                url: '/jd/advert/edit',
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
<script type="text/html" id="createTimeTpl">
    {{ dateFormat( d.createTime) }}
</script>
<script type="text/html" id="updateTimeTpl">
    {{ dateFormat( d.updateTime) }}
</script>
<script type="text/html" id="endTimeTpl">
    {{ dateFormat( d.endTime) }}
</script>
<script type="text/html" id="startTimeTpl">
    {{ dateFormat( d.startTime) }}
</script>
</html>
