<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>主页</title>
    <link rel="stylesheet" href="/css/shouye.css">
</head>
<body>
<div class="bar">
    <div class="logo-title">欢迎注册</div>
    <div class="zhanghao_link">
        已有账号？<a href="/user/login.jsp" target="_blank" class="a_link"
    >请登录></a
    >
    </div>
</div>

<form class="login_form layui-form">
    <div class="login-tab">账号注册</div>
    <div class="layui-form-item">
        <div class="inputdiv">
            <i class="layui-icon layui-icon-username"></i>
            <input
                    type="text"
                    id="username"
                    name="username"
                    lay-verify="required"
                    placeholder="请输入至少8个字符的用户名"
                    autocomplete="off"
                    class="layui-input"
                    onblur="checkValid('#username', '#errorName')"
            />
        </div>
        <div class="error-msg"><span id="errorName"></span></div>
    </div>
    <div class="layui-form-item">
        <div class="inputdiv">
            <i class="layui-icon layui-icon-password"> </i>
            <input
                    type="password"
                    id="password"
                    name="password"
                    lay-verify="required"
                    placeholder="请输入密码"
                    autocomplete="off"
                    class="layui-input"
                    onblur="checkValid('#password', '#errorPass')"
            />
        </div>
        <div class="error-msg"><span id="errorPass"></span></div>
    </div>
    <div class="layui-form-item">
        <button
                class="layui-btn layui-btn-fluid"
                lay-submit
                lay-filter="register"
        >
            注册
        </button>
    </div>
</form>
<div class="copyright">Copyright © 2004-2022 京东JD.com 版权所有</div>
</body>
<script>
    layui.form.on("submit(register)", function (formdata) {
        console.log(formdata.field);
        if()
        layui.$.ajax({
          url: "/check_login",
          data: formdata.field,
          type: "post",
          dataType: "json",
          success : function(json){
            if(json.code == "0"){
              layui.layer.msg("登录成功");
              window.location.href = json.redirect_url;
            } else{
              layui.layer.msg(json.message);
            }
          }

        })
        return false; //false不提交
    });
</script>
</html>