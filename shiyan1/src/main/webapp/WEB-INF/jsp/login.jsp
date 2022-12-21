<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/10/10
  Time: 上午 09:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="/resources/layui/css/layui.css">
    <link rel="stylesheet" href="/css/login.css">
    <script src="/resources/jquery.3.3.1.min.js"></script>
    <script type="text/javascript" src="/js/validation.js"></script>
    <title>登录页面</title>
    <script type="text/javascript">
        function checkSubmit(){
            var r2 = checkEmpty('#password', '#errorInput');
            var r1 = checkEmpty('#userName', '#errorInput');

            if(r1 && r2){
                return true;
            } else{
                return false;
            }
        }
    </script>
</head>

<body>
<div class="login-wrapper">
    <div class="model">
        <form  onsubmit="return checkSubmit()" class="layui-form" >
            <div class="title">欢迎登录</div>
            <div class="msg-wrapper" >
                <div  id="errorInput"></div>
            </div>
            <div class="layui-form-item">
                <div class="inputdiv">
                    <i class="layui-icon layui-icon-username"></i>
                    <input type="text" id="userName" name="userName" placeholder="请输入用户名"
                           autocomplete="off" class="layui-input"  onblur="checkEmpty('#userName', '#errorInput')"
                         />
                </div>
            </div>
            <div class="layui-form-item">
                <div class="inputdiv">
                    <i class="layui-icon  layui-icon-password">
                    </i>
                    <input type="password" id="password" name="password" placeholder="请输入密码"
                           autocomplete="off" class="layui-input"  onblur="checkEmpty('#password', '#errorInput')"
                          />
                </div>
            </div>
            <div class="layui-form-item">
                <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="login" >登录</button>
            </div>
            <div class="register">
                <a href="/user/register.jsp">
                    <i
                            class="layui-icon layui-icon-right"
                            style="font-size: 16px; font-weight: 700; color: red"
                    ></i>
                    立即注册
                </a>
            </div>
        </form>
    </div>
</div>
</body>
<script src="/resources/layui/layui.js"></script>
<script src="/resources/sweetalert2.all.min.js"></script>
<script>
    layui.form.on("submit(login)", function (formdata) {
      if(checkSubmit()) {
          layui.$.ajax({
              url: "/user/check_login",
              data:formdata.field,
              type: "post",
              dataType: "json",
              success: function (json) {
                  if (json.code === 200) {
                      layui.layer.msg("登录成功", {
                          icon: 6,
                          timer: 1200,
                          offset: 't',
                      })
                      window.location = "/home.jsp";
                  } else {
                      layui.layer.msg(json.msg, {
                          icon: 2,
                          timer: 1500,
                          offset: 't',
                      })
                  }
              }

          })
          return false;
      }
    })


</script>

</html>
