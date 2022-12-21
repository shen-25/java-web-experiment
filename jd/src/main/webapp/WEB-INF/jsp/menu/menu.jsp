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
    <link rel="stylesheet" href="/jd/css/menu.css"/>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script src="/jd/resources/sweetalert2.all.min.js"></script>
    <title>菜单管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form">

        <div class="layui-inline">
            <label class="layui-form-label inline-name">菜单名称</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="menuName"
                        placeholder="请输入菜单名称"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label inline-name">路由地址</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="url"
                        placeholder="请输入路由地址"

                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label inline-name">菜单状态</label>
            <div class="layui-input-inline">
                <select name="menuState">
                    <option value="-1">所有</option>
                    <option value="0">正常</option>
                    <option value="1">停用</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style">
                <button class="layui-btn" lay-submit lay-filter="roleQueryFilter">查询</button>
                <button type="reset" class="layui-btn layui-btn-primary">
                    重置
                </button>
            </div>
        </div>
    </form>
</div>

<div class="layui-btn-container">

    <c:forEach items="${buttons}" var="btn">
        <c:if test="${btn.menuName ==  '新增'}">
            <button
                    type="button"
                    class="layui-btn layui-btn-sm layui-btn-normal create-button"
                    id="create-menu" data-menu-add-url="${btn.getUrl()}">${btn.menuName}
            </button>
        </c:if>
    </c:forEach>
</div>

<div class="base-table">
    <table id="menuTable" lay-filter="menuOpera" class="menuTable"></table>

    <script type="text/html" id="menuBar">
        <c:forEach items="${buttons}" var="btn">
            <c:if test="${btn.menuName ==  '编辑'}">

                <a class="layui-btn layui-btn-xs" data-menu-edit-url="${btn.getUrl()}"
                   lay-event="edit">${btn.menuName}</a>
            </c:if>
            <c:if test="${btn.menuName ==  '删除'}">
                <a class="layui-btn layui-btn-danger layui-btn-xs" data-menu-del-url="${btn.getUrl()}"
                   lay-event="del">${btn.menuName}</a>
            </c:if>
        </c:forEach>
    </script>

</div>
</div>

<div id="menuDialog" style="display: none; padding: 18px 35px 0px 30px">
    <form class="layui-form" lay-filter="createMenuFilter">
        <div class="layui-form-item" style="display: none">
            <label class="layui-form-label">菜单ID</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="menuId"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">父级菜单</label>
            <div class="layui-input-block">
                <select name="parentId">
                    <option value="0">不选,则直接创建一级菜单</option>
                    <c:forEach items="${menuSelectReqList}" var="menuSelect" >
                    <option value="${menuSelect.getMenuId()}">${menuSelect.getGrade()}--${menuSelect.getMenuName()}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">类型</label>
            <div class="layui-input-block">
                <select name="menuType" class="form-menuType">
                    <option value="0">菜单</option>
                    <option value="1">按钮</option>
                    <option value="2">资源</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">菜单名称</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="menuName"
                        lay-verify="menuName"
                        autocomplete="off"
                        placeholder="请输入菜单名称"
                        class="layui-input form-menuName"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">路由地址</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="url"
                        autocomplete="off"
                        placeholder="请输入路由地址"
                        class="layui-input form-url"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图标</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="icon"
                        lay-verify="title"
                        autocomplete="off"
                        placeholder="请输入图标"
                        class="layui-input form-icon"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">排序</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="menuOrder"
                        lay-verify="title"
                        autocomplete="off"
                        placeholder="请输入排序"
                        class="layui-input form-menuOrder"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">菜单状态</label>
            <div class="layui-input-block form-menuState">
                <input type="radio" name="menuState" value="0" title="正常" checked="">
                <input type="radio" name="menuState" value="1" class="form-menuState" title="停用">
            </div>
        </div>
        <div>
            <div style="display: none">
                <button type="button" id="menuSubmit" lay-submit lay-filter="menuInput">立即提交1</button>
                <button type="button" id="menuSubmit2" lay-submit lay-filter="menuInput2">立即提交2</button>
                <button type="reset" class="layui-btn layui-btn-primary" id="menu-from-reset">重置</button>
            </div>
        </div>
    </form>
</div>
</body>
<script src="/jd/js/menu.js"></script>
<script>
    layui.use(['table', 'form'], function () {
        const table = layui.table;
        const layer = layui.layer;
        const form = layui.form;
        let menuEditUrl = null;
        let menuAddUrl = null;
        layui.use("form", function () {
            let form = layui.form;
            form.on("submit(roleQueryFilter)", function (data) {
                let screen = data.field;
                table.reload('menuTable', {
                    url: '/jd/sys/menu/list?menuName=' + screen.menuName + '&url=' + screen.url + '&menuState=' + screen.menuState,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#menuTable",
            height: 513,
            url: '/jd/sys/menu/list', //数据接口
            page: true,
            id: "menuTable",
            parseData: function (res) { //res 即为原始返回的数
                return {
                    "code": 0, //解析接口状态,为0才显示数据
                    "msg": res.msg, //解析提示文本
                    "count": res.data.total, //解析数据长度
                    "data": res.data.records//解析数据列表
                };
            },
            cols: [
                [
                    {field: "menuId", title: "菜单ID", width: 85},
                    {field: "menuName", title: "菜单名称", width: 180},
                    {
                        field: "menuType", title: "类型", width: 95, templet: function (d) {
                            let menuType = (d.menuType);
                            if (menuType == 0) {
                                return '<span class="layui-btn layui-btn-normal layui-btn-xs">菜单</span>';
                            } else if (menuType == 1) {
                                return '<span class="layui-btn layui-btn-primary layui-btn-xs">按钮</span>';
                            } else if (menuType == 2) {
                                return '<span class="layui-btn layui-btn-primary layui-btn-xs layui-border-green">资源</span>';
                            }
                        }
                    },
                    {field: "url", title: "路由地址", width: 225},
                    {
                        field: "icon", title: "图标", width: 80, templet: function (d) {
                            let icon = (d.icon);
                            return "<i class='layui-icon " + icon + " '></i>";
                        }
                    },
                    {
                        field: "menuState", title: "菜单状态", width: 100, templet: function (d) {
                            let state = (d.menuState);
                            if (state == 0) {
                                return '<span class="layui-btn  layui-btn-xs">正常</span>';
                            } else {
                                return '<span class="layui-btn layui-btn-primary layui-btn-xs">停用</span>';
                            }
                        }
                    },
                    {field: "parentId", title: "父级菜单", width: 100},
                    {field: "menuOrder", title: "排序", width: 80},
                    {field: "createTime", title: "创建时间", width: 188, templet: "#createTimeTpl"},
                    {field: "updateTime", title: "更新时间", width: 188, templet: "#updateTimeTpl"},
                    {
                        fixed: "right",
                        title: "操作",
                        width: 118,
                        minWidth: 118,
                        toolbar: "#menuBar",
                    },
                ],
            ],
        });

        table.on("tool(menuOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                let func = layui.$(this);
                let menuDelUrl = "/jd" + func.data("menu-del-url");
                layer.confirm("你确定要删除名称[" + data.menuName + "]的菜单吗？它的下级菜单也会删除喔？",
                    {icon:3}, function(index){
                        $.ajax({
                            url: menuDelUrl,
                            data: {"menuId": data.menuId},
                            type: 'post',
                            dataType: 'json',
                            success: function (json) {
                                if (json.code === 200) {
                                    $(".layui-laypage-btn").click();
                                    layer.close(index);
                                    layui.layer.msg("删除成功", {
                                        icon: 1,
                                        area: '100px',
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
            } else if (obj.event === "edit") {
                let func = layui.$(this);
                menuEditUrl = "/jd" + func.data("menu-edit-url");
                layer.open({
                    title: "编辑",
                    type: 1,
                    area: ["620px", "500px"],
                    content: $("#menuDialog"),
                    btn: ["提交", "取消"],
                    success: function (layero, index) {
                        form.val('createMenuFilter', {
                            "menuName": data.menuName,
                            "menuType": data.menuType,
                            "icon": data.icon,
                            "url": data.url,
                            "menuId": data.menuId,
                            "menuState": data.menuState,
                            "menuOrder": data.menuOrder,
                            "parentId": data.parentId
                        });
                    },
                    yes: function (index, layero) {
                        $("#menuSubmit2").click();

                    }, cancel: function (index, layero) {
                        layer.close(index)
                        return false;
                    },
                    end: function (index, layero) {
                        $("#menuDialog").hide();
                        layer.close(index);
                        $('#menu-from-reset').click();
                    }

                });
            }
        });

        $("#create-menu").click(function () {
            let func = layui.$(this);
            menuAddUrl = "/jd" + func.data("menu-add-url");
            layer.open({
                title: "新增菜单",
                type: 1,
                area: ["620px", "500px"],
                content: $("#menuDialog"),
                btn: ["提交", "取消"],
                success: function (layero, index) {

                },
                yes: function (index, layero) {
                    $("#menuSubmit").click();
                },
                cancel: function (index, layero) {
                    layer.close(index)
                    return false;
                },
                end: function (index, layero) {
                    $("#menuDialog").hide();
                    $("#menu-from-reset").click();
                    layer.close(index);
                    form.render();
                }
            });
        });
        form.on('submit(menuInput)', function (data) {
            $.ajax({
                url: menuAddUrl,
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

        form.on('submit(menuInput2)', function (data) {
            $.ajax({
                url: menuEditUrl,
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
