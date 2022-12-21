<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="/jd/resources/layui/css/layui.css"/>
    <link rel="stylesheet" href="/jd/css/index.css"/>
    <link rel="stylesheet" href="/jd/css/userform.css"/>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script src="/jd/resources/sweetalert2.all.min.js"></script>
    <script src="/jd/resources/layui/xm-select.js"></script>
    <script src="/jd/resources/art-template.js"></script>

    <title>用户管理</title>

</head>
<body>
<div class="user-manage">
    <div class="query-form">
        <form class="layui-form" >
            <div class="layui-form-item">
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
                    <label class="layui-form-label inline-name">用户名</label>
                    <div class="layui-input-inline">
                        <input
                                type="text"
                                name="userName"
                                id="userName"
                                lay-verify="text"
                                placeholder="请输入用户名称"
                                autocomplete="off"
                                class="layui-input inline-input"
                        />
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">用户状态</label>
                    <div class="layui-input-block">
                        <select name="state" lay-verify="">
                            <option value="0">所有</option>
                            <option value="1" selected>在职</option>
                            <option value="3">试用期</option>
                            <option value="2">离职</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <div class="layui-input-block inline-input inline-style">
                        <button
                                class="layui-btn"
                                lay-submit
                                lay-filter="userForm"
                                id="screen"
                        >
                            查询
                        </button>
                        <button
                                type="reset"
                                class="layui-btn layui-btn-primary"
                        >
                            重置
                        </button>
                    </div>
                </div>
            </div>
        </form>

    </div>
</div>
<div class="base-table">
    <table id="userTable" lay-filter="userOpera" class="userTable"></table>

    <script type="text/html" id="userBar">
        <c:forEach items="${buttons}" var="btn">
            <c:if test="${btn.menuName == '编辑'}">
               <a class="layui-btn layui-btn-xs user-edit" lay-event="edit">${btn.getMenuName()}</a>
            </c:if>

            <c:if test="${btn.menuName ==  '删除'}">
               <a class="layui-btn layui-btn-danger layui-btn-xs user-delete" lay-event="del" data-user-delete-url="${btn.getUrl()}" >${btn.getMenuName()}</a>
            </c:if>
        </c:forEach>
    </script>
    <script type="text/html" id="toolbarDemo">
        <div class="layui-btn-container">
<c:forEach items="${buttons}" var="btn">
    <c:if test="${btn.menuName == '新增'}">
                    <button
                            type="button"
                            class="layui-btn layui-btn-sm layui-btn-normal button-radius-style"
                            lay-event="add"
                            id="create-user">
                        ${btn.getMenuName()}
                  </button>
    </c:if>
    <c:if test="${btn.menuName == '删除'}">
            <button
                    class="layui-btn button-radius-style layui-btn-sm  layui-btn-danger"
                    lay-event="getCheckData"
                    id="user-batchDelete"
            >
                批量${btn.getMenuName()}
            </button>
</c:if>
</c:forEach>
        </div>
    </script>
</div>
</body>

<script>

 const prefix = "/jd"

    layui.use("table", function () {
        const table = layui.table;
        const layer = layui.layer;
        layui.use("form", function () {
            let form = layui.form;
            form.on("submit(userForm)", function (data) {
                // layer.msg(JSON.stringify(data.field));
                let screen = data.field;
                table.reload('userTable', {
                    url: prefix + '/sys/user/list?userId=' + screen.userId + '&userName=' + screen.userName + '&state=' + screen.state,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        //第一个实例
        table.render({
            elem: "#userTable",
            height: 582,
            url: prefix + '/sys/user/list', //数据接口
            page: true, //开启分页
            parseData: function (res) { //res 即为原始返回的数
                return {
                    "code": 0, //解析接口状态,为0才显示数据
                    "msg": res.msg, //解析提示文本
                    "count": res.data.total, //解析数据长度
                    "data": res.data.records//解析数据列表
                };
            },
            toolbar: "#toolbarDemo",
            cols: [
                [
                    //表头
                    {type: "checkbox", fixed: "left"},
                    {
                        field: "userId",
                        title: "用户ID",
                        width: 95,
                        sort: true,
                        fixed: "left",
                    },
                    {field: "userName", title: "用户名", width: 135},
                    {field: "userEmail", title: "用户邮箱", width: 198},
                    {field: "sex", title: "性别", width: 78, templet: '#forSexTpl'},
                    {field: "state", title: "用户状态", width: 98, templet: '#forStateTpl'},
                    {field: "job", title: "职位", width: 145},
                    {field: "createTime", title: "注册时间", width: 180, sort: true, templet: "#createTimeTpl"},
                    {field: "lastLoginTime", title: "最后登录时间", width: 180, sort: true, templet: "#lastLoginTimeTpl"},
                    {
                        fixed: "right",
                        title: "操作",
                        width: 125,
                        minWidth: 125,
                        toolbar: "#userBar",
                    },
                ],
            ],
        });

        table.on("tool(userOpera)", function (obj) {
            const data = obj.data;
            if (obj.event === "del") {
                let func = layui.$(this);
                let userDelUrl =  func.data("user-delete-url");
                layer.confirm("你确定要删除ID[" + data.userId + "]的用户吗？", {icon:3},
                    function(index){
                    layer.close(index);
                    let userDelete = {
                        "userIds": data.userId,
                    }
                    $.ajax({
                        url: "/jd" + userDelUrl,
                        data: userDelete,
                        type: 'post',
                        dataType: 'json',
                        success:  function (json) {
                            if (json.code === 200) {
                                layer.msg("删除成功", {
                                    icon: 1,
                                    time: 1300,
                                    offset: 't',
                                })
                            }
                            $(".layui-laypage-btn").click();
                        }
                    });

                });
            } else if (obj.event === "edit") {
                //console.log(JSON.stringify(data))
                layer.open({
                    title: "编辑",
                    type: 2,
                    area: ["55%", "90%"],
                    content: prefix + "/sys/user/showEdit",
                    btn: ["提交", "取消"],
                    success: function (layero, index) {
                        let div = layero.find("iframe").contents().find(".layui-form");
                        // console.log(data.userId + "dddd");
                        // console.log(JSON.stringify(data))
                        div.find("#userIdSubmit").val(data.userId);

                        div.find("#userName").val(data.userName);
                        div.find("#userEmail").val(data.userEmail);
                        div.find("#role").val(data.role)
                        div.find("#sex").val(data.sex)
                        div.find("#job").val(data.job)
                        div.find("#deptId").val(data.deptId)
                        div.find("#state").val(data.state)
                        div.find("#mobile").val(data.mobile)
                        div.find("#userIdSubmit").click();
                    },
                    yes: async function (index, layero) {
                        let div = layero.find("iframe").contents().find(".layui-form");
                        div.find(".layui-btn").click();
                        $(".layui-laypage-btn").click();
                    }

                });
            }
        });

        table.on("toolbar(userOpera)", function (obj) {
            const form = layui.form;
            const checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
            switch (obj.event) {
                case "getCheckData":
                    let data = checkStatus.data; //获取选中行数据
                    if (data === null || data.length === 0) {

                        layer.msg("至少选择一行", {

                            icon: 2,
                            time: 1500,
                            area: '100px',
                            offset: 't',
                        })
                        return;
                    }
                    let user = '';
                    for (let i = 0; i < data.length; i++) {
                        user += data[i].userId;
                        if (i !== data.length - 1)
                            user += ','
                    }
                    const ans = {
                        "userIds": user,
                    }
                    swal({
                        title: "你确定要批量删除用户吗？",
                        text: "一旦删除，您将无法恢复用户！",
                        icon: "warning",
                        buttons: true,
                        dangerMode: true,
                    }).then((willDelete) => {
                        if (willDelete) {
                            $.ajax({
                                url: prefix + '/sys/user/delete',
                                type: 'post',
                                data: ans,
                                dataType: 'json',
                                success: async function (json) {
                                    if (json.code === 200) {
                                        await swal({
                                            text: "批量" + json.data + "条删除成功！",
                                            type: "success",
                                            buttons: false,
                                            timer: 1500,
                                        })
                                        window.location.reload()
                                    }
                                }
                            })
                        } else {
                            swal("删除失败", {
                                icon: "error",
                                showCancelButton: false,
                                showConfirmButton: false,
                                buttons: false,
                                timer: 1500,
                            });
                        }
                    })
                    break;
                case "add":
                    layer.open({
                        title: "新增用户",
                        type: 2,
                        area: ["55%", "90%"],
                        content: prefix + "/sys/user/showAdd",
                        move: false,
                        btn: ["提交", "取消"],
                        yes: async function (index, layero) {
                            let div = layero.find("iframe").contents().find(".layui-form");
                            div.find(".layui-btn").click();
                        }
                    });
                    break;
            }
        });
    });
</script>
<script src="/jd/js/dateConversion.js"></script>
<script type="text/html" id="forSexTpl">
    {{# if(d.sex === 1){ }}
    {{d.sex="男"}}
    {{#  } else if(d.sex === 2){ }}
    {{d.sex="女"}}
    {{#  } }}
</script>

<script type="text/html" id="forStateTpl">
    {{# if(d.state === 1){ }}
    {{d.state="在职"}}
    {{#  } else if(d.state === 2){ }}
    {{d.state="离职"}}
    {{#  } else if(d.state === 3){ }}
    {{d.state="试用期"}}
    {{# } }}
</script>
<script type="text/html" id="createTimeTpl">
    {{ dateFormat( d.createTime) }}
</script>
<script type="text/html" id="lastLoginTimeTpl">
    {{ dateFormat( d.lastLoginTime) }}
</script>

</html>
  