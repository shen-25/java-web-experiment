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
    <link rel="stylesheet" href="/jd/css/role.css"/>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script src="/jd/resources/sweetalert2.all.min.js"></script>
    <title>角色管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form" action="">
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label inline-name">角色名称</label>
                <div class="layui-input-inline">
                    <input
                            type="text"
                            name="roleName"
                            placeholder="请输入角色名称"
                            class="layui-input inline-input"
                    />
                </div>
            </div>
            <div class="layui-inline">
                <div class="layui-input-block inline-input inline-style">
                    <button class="layui-btn" lay-submit lay-filter="roleForm">查询</button>
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
        <c:if test="${btn.menuName ==  '新增'}">
            <button
                    type="button"
                    class="layui-btn layui-btn-sm layui-btn-normal create-button"
                    id="create-role"> ${btn.getMenuName()}
            </button>
        </c:if>
    </c:forEach>
</div>

<div class="base-table">
    <table id="roleTable" lay-filter="roleOpera" class="roleTable"></table>

    <script type="text/html" id="roleBar">
        <c:forEach items="${buttons}" var="btn">
            <c:if test="${btn.menuName ==  '编辑'}">
                <a class="layui-btn layui-btn-xs role-edit" lay-event="edit" data-role-edit-url="${btn.getUrl()}">
                        ${btn.getMenuName()}
                </a>
            </c:if>
            <c:if test="${btn.menuName ==  '设置权限'}">

                <a class="layui-btn layui-btn-xs layui-btn-normal set-power" lay-event="setPower"
                   data-role-set-power-url="${btn.getUrl()}">
                        ${btn.getMenuName()}
                </a>
            </c:if>
            <c:if test="${btn.menuName ==  '删除'}">
                <a class="layui-btn layui-btn-danger layui-btn-xs role-delete" data-role-delete-url="${btn.getUrl()}"
                   lay-event="del">
                        ${btn.getMenuName()}
                </a>
            </c:if>
        </c:forEach>
    </script>

</div>
</div>

<div id="roleDialog" style="display: none; padding: 18px 35px 0px 0px">
    <form class="layui-form" lay-filter="createRoleFilter">
        <div class="layui-form-item" style="display: none">
            <label class="layui-form-label">角色ID</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="roleId"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">角色名称</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="roleName"
                        lay-verify="roleName"
                        autocomplete="off"
                        placeholder="请输入角色名称"
                        id="roleNameForm"
                        class="layui-input"
                />
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">描述</label>
            <div class="layui-input-block">
            <textarea
                    name="remark"
                    lay-verify="remark"
                    placeholder="请输入描述"
                    class="layui-textarea"
                    autocomplete="off"
                    id="formDesc"
            ></textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">是否启动</label>
            <div class="layui-input-block" id="state">
                <input type="radio" name="state" value="0" title="启动" checked>
                <input type="radio" name="state" value="1" title="禁用">
            </div>
        </div>
        <div class="layui-form-item" style="display:none">
            <div class="layui-input-block">
                <button
                        type="button"
                        class="layui-btn"
                        lay-submit
                        id="roleSubmit"
                        lay-filter="roleInput"
                >
                    立即提交
                </button>
                <button
                        type="button"
                        class="layui-btn"
                        lay-submit
                        id="roleSubmit2"
                        lay-filter="roleInput2">
                    立即提交2
                </button>
            </div>
        </div>
    </form>
</div>
</body>
<script src="/jd/js/role.js"></script>
<script>
    layui.use(['table'], function () {
        const table = layui.table;
        const layer = layui.layer;
        const form = layui.form;

        let roleEditUrl = null;
        let roleAddUrl = null;
        let roleSetPowerUrl = null;
        let roleDelUrl = null;
        <c:forEach items="${buttons}" var="btn">

        <c:if test="${btn.menuName eq  '编辑'}">
        roleEditUrl = "/jd" +  "${btn.getUrl()}"
        </c:if>
        <c:if test="${btn.menuName eq  '新增'}">
        roleAddUrl = "/jd" + "${btn.getUrl()}"
        </c:if>
        <c:if test="${btn.menuName eq  '删除'}">
        roleDelUrl = "/jd" + "${btn.getUrl()}"
        </c:if>
        <c:if test="${btn.menuName eq  '设置权限'}">
        roleSetPowerUrl = "/jd" + "${btn.getUrl()}"
        </c:if>
        </c:forEach>


        layui.use("form", function () {
            let form = layui.form;
            form.on("submit(roleForm)", function (data) {
                // layer.msg(JSON.stringify(data.field));
                let screen = data.field;
                table.reload('roleTable', {
                    url: '/jd/sys/role/list?roleName=' + screen.roleName,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#roleTable",
            height: 525,
            page: true, //开启分页
            url: '/jd/sys/role/list',
            parseData: function (res) { //res 即为原始返回的数
                return {
                    "code": 0, //解析接口状态,为0才显示数据
                    "msg": res.msg, //解析提示文本
                    "count": res.data.total, //解析数据长度
                    "data": res.data.records//解析数据列表
                };
            },
            id: "roleTable",
            cols: [
                [
                    {field: "roleName", title: "角色名称", width: 177},
                    {field: "remark", title: "描述", width: 410},
                    {
                        field: "state", title: "状态", width: 98, templet: function (d) {
                            let state = (d.state);
                            if (state === 0) {
                                return "正常";
                            } else if (state === 1) {
                                return "停用";
                            }
                        }
                    },
                    {field: "createTime", title: "创建时间", width: 195, templet: "#createTimeTpl"},
                    {field: "updateTime", title: "更新时间", width: 195, templet:  function (d) {
                          let date = d.updateTime;
                           return  new Date(date).toLocaleString();
                        }
                    },
                    {
                        fixed: "right",
                        title: "操作",
                        width: 190,
                        minWidth: 190,
                        toolbar: "#roleBar",
                    },
                ],
            ],
        });

        table.on("tool(roleOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                swal({
                    title: "你确定要删除名称[" + data.roleName + "]的角色吗？",
                    text: "一旦删除，您将无法恢复该角色！",
                    icon: "warning",
                    buttons: true,
                    dangerMode: true,
                }).then((willDelete) => {
                    if (willDelete) {
                        $.ajax({
                            url: roleDelUrl,
                            data: {"roleId": data.roleId},
                            type: 'post',
                            dataType: 'json',
                            success: async function (json) {
                                if (json.code === 200) {
                                    await swal("删除成功", {
                                        icon: "success",
                                        showCancelButton: false,
                                        showConfirmButton: false,
                                        buttons: false,
                                        timer: 1500,
                                    });
                                  $(".layui-laypage-btn").click();
                            } else {
                                    swal(json.msg, {
                                        icon: "error",
                                        showCancelButton: false,
                                        showConfirmButton: false,
                                        buttons: false,
                                        timer: 1200,
                                    });
                                }
                            }
                        });
                    } else {
                        swal("删除失败", {
                            icon: "error",
                            showCancelButton: false,
                            showConfirmButton: false,
                            buttons: false,
                            timer: 1500,
                        });
                    }
                });
            } else if (obj.event === "edit") {
                layer.open({
                    title: "编辑",
                    type: 1,
                    area: ["440px", "360px"],
                    content: $("#roleDialog"),
                    btn: ["提交", "取消"],
                    success: function (layero, index) {
                        form.val('createRoleFilter', {
                            "roleId": data.roleId,
                            "roleName": data.roleName,
                            "remark": data.remark,
                            "state": data.state

                        });
                        $("#roleNameForm").addClass("edit-role-name-style")
                        $("#roleNameForm").attr("disabled", "disabled");
                    },
                    yes: function (index, layero) {
                        $("#roleSubmit2").click();
                    },
                    end: function (index, layero) {
                        $("#roleDialog").hide();
                        $("#roleNameForm").removeClass("edit-role-name-style")
                        $("#roleNameForm").removeAttr("disabled");
                        form.val('createRoleFilter', {
                            "roleId": "",
                            "roleName": "",
                            "remark": "",
                            "state": 0
                        });
                    }

                });
            } else if (obj.event === "setPower") {
                layer.open({
                    title: "设置权限",
                    type: 2,
                    area: ["55%", "94%"],
                    content: roleSetPowerUrl,
                    move: false,
                    btn: ["提交", "取消"],
                    success: function (layero, index) {
                        console.log(data.roleId);
                        let div = layero.find("iframe").contents();
                        div.find("#treeRoleId").val(data.roleId);
                        div.find("#treeRoleName").text(data.roleName);
                    },
                    yes: function (index, layero) {
                        let div = layero.find("iframe").contents();
                        div.find("#getData").click();
                    }
                });
            }
        });

        $("#create-role").click(function () {
            layer.open({
                title: "新增角色",
                type: 1,
                area: ["440px", "360px"],
                content: $("#roleDialog"),
                btn: ["提交", "取消"],
                yes: function (index, layero) {
                    $("#roleSubmit").click();
                },
                cancel: function (index, layero) {
                    layer.close(index)
                    return false;
                },
                end: function (index, layero) {
                    $("#roleDialog").hide();
                    $(".layui-laypage-btn").click();
                    layer.close(index);
                    form.val('createRoleFilter', {
                        "roleName": "",
                        "remark": "",
                        "state": 0
                    });
                    return false;
                }
            });
        })
        form.on('submit(roleInput)', function (data) {
            $.ajax({
                url: roleAddUrl,
                type: 'post',
                data: JSON.stringify(data.field),
                contentType: 'application/json',
                dataType: 'json',
                success: function (json) {
                    if (json.code === 200) {

                        layer.msg("新增成功", {
                            icon: 1,
                            time: 1300,
                            offset: 't',
                        });
                        layui.layer.closeAll();
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


        form.on('submit(roleInput2)', function (data) {
            $.ajax({
                url: roleEditUrl,
                type: 'post',
                data: JSON.stringify(data.field),
                contentType: 'application/json',
                dataType: 'json',
                success: function (json) {
                    layui.layer.closeAll();
                    if (json.code === 200) {
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
