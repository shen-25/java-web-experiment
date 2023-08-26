<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="/jd/resources/layui/css/layui.css">
    <link rel="stylesheet" href="/jd/css/login.css">
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script type="text/javascript" src="/jd/js/validation.js"></script>
    <title>登录</title>
    <script type="text/javascript">
        function checkSubmit() {
            var r2 = checkEmpty('#password', '#errorInput');
            var r1 = checkEmpty('#userName', '#errorInput');
            var r3 = checkEmpty('#verifyCode', '#errorInput');
            if (r1 && r2 && r3) {
                return true;
            } else {
                return false;
            }
        }
    </script>
</head>

<body>
<div class="login-wrapper">
    <div class="model">
        <form  class="layui-form">
            <div class="title">manager</div>
            <div class="msg-wrapper">
                <div id="errorInput"></div>
            </div>
            <div class="layui-form-item">
                <div class="inputdiv">
                    <i class="layui-icon layui-icon-username"></i>
                    <input type="text" id="userName" name="userName" placeholder="请输入用户名"
                           autocomplete="off" class="layui-input" onblur="checkEmpty('#userName', '#errorInput')"
                           oninput="onInput('#userName')"/>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="inputdiv">
                    <i class="layui-icon  layui-icon-password">
                    </i>
                    <input type="password" id="password" name="password" placeholder="请输入密码"
                           autocomplete="off" class="layui-input" onblur="checkEmpty('#password', '#errorInput')"
                           oninput="onInput('#password')"/>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="inputdiv float-left">
                    <i class="layui-icon  layui-icon-vercode">
                    </i>
                    <input type="text" id="verifyCode" name="vc" placeholder="请输入右侧验证码" autocomplete="off"
                           class="layui-input" onblur="checkEmpty('#verifyCode', '#errorInput')"
                           oninput="onInput('#verifyCode')"/>
                </div>
                <div class="float-right">
                    <img id="imgVerifyCode" src="/jd/sys/user/verify_code" style="width: 120px;height:40px;cursor: pointer"
                    >
                </div>
            </div>

            <div class="layui-form-item">
                <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="login">登录</button>
            </div>
        </form>
    </div>
</div>
</body>
<script src="/jd/resources/layui/layui.js"></script>
<script src="/jd/resources/sweetalert2.all.min.js"></script>
<script>
    $("#imgVerifyCode").click(function () {
        reloadVerifyCode();
    })

    function reloadVerifyCode() {
        $("#imgVerifyCode").attr("src", "/jd/sys/user/verify_code?ts=" + new Date().getTime());

    }

    layui.form.on("submit(login)", function (formdata) {

        if (checkSubmit()) {
            console.log("ddd");
            console.log(checkSubmit());
           $.ajax({
                url: "/jd/sys/user/check_login",
                data: formdata.field,
                type: "post",
                dataType: "json",
                success: function (json) {
                    if (json.code === 200) {
                        layui.layer.msg("登录成功", {
                            icon: 1,
                            area: '100px',
                            time: 1200,
                            offset: 't',
                        })
                        setTimeout(function () {
                            window.location = "/jd/home";
                        }, 1500);
                    } else {
                        layui.layer.msg(json.msg, {
                            icon: 2,
                            time: 1500,
                            offset: 't',
                        })
                    }
                }

            });
        }
        return false;
    })
</script>

</html>