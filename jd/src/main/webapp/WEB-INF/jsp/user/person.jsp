<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/11/23
  Time: 下午 01:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="/jd/resources/layui/css/layui.css"/>
    <link rel="stylesheet" href="/jd/css/index.css"/>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <title>修改个人信息</title>
</head>
<style>
    body {
        background-color: #fff;
    }
    .wrapper {
        padding-top: 20px;
        width: 80%;
    }
    .user-btn {
        margin: 35px 0px 0px 115px;
    }
</style>
<body>
<div class="wrapper">
    <form class="layui-form">
        <div class="layui-form-item">
            <label class="layui-form-label">个人账号</label>
            <div class="layui-input-block">
                <input
                        type="type"
                        name="userName"
                        disabled
                        id="userName"
                        lay-verify="title"
                        autocomplete="off"
                        class="layui-input disable-bgcolor"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">邮箱</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="userEmail"
                        lay-verify="required"
                        id="userEmail"
                        placeholder="请输入邮箱"
                        autocomplete="off"
                        class="layui-input"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">原密码</label>
            <div class="layui-input-block">
                <input
                        type="password"
                        name="password"
                        lay-verify="required"

                        placeholder="请输入原密码"
                        autocomplete="off"
                        class="layui-input"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">新密码</label>
            <div class="layui-input-block">
                <input
                        type="password"
                        name="newPassword"
                        lay-verify="required"
                        placeholder="请输入密码,长度为6-20个字符"
                        autocomplete="off"
                        class="layui-input"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">确认新密码</label>
            <div class="layui-input-block">
                <input
                        type="password"
                        name="confirmPassword"
                        lay-verify="required"
                        placeholder="请再次输入新密码"
                        autocomplete="off"
                        class="layui-input"
                />
            </div>
        </div>
        <div class="layui-form-item user-btn">
            <button
                    class="layui-btn layui-btn-normal"
                    lay-submit
                    lay-filter="edit-user">
                提交
            </button>
            <button class="layui-btn layui-btn-primary" id="cancel-btn">
                取消
            </button>
            <button style="display: none" id="reset-btn">
                重置
            </button>
        </div>
    </form>
</div>
</body>
<script>
    $(function (){
        $("#userName").val('${person.userName}')
        $("#userEmail").val('${person.userEmail}')
    });

    layui.form.on("submit(edit-user)", function (data) {
        console.log(JSON.stringify(data.field))
        layui.$.ajax({
            url: "/jd/sys/user/personEdit",
            data: data.field,
            type: "post",
            dataType: "json",
            success: function (json) {
                if (json.code === 200) {
                    layer.msg("修改个人信息成功", {
                        icon: 1,
                        time: 1300,
                        offset: 't',
                    });
                    setTimeout(function () {
                        window.location.href = "/jd/sys/user/person";
                    }, 1400);

                } else {
                    layer.msg(json.msg, {
                        icon: 2,
                        time: 1300,
                        offset: 't',
                    });
                }
            },
        });
        return false;
    });

    $("#cancel-btn").click(function () {
        $("#reset-btn").click();
    });
</script>
</html>

