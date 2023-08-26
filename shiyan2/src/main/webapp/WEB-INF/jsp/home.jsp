<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/10/10
  Time: 下午 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>欢迎</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
    <link rel="stylesheet" href="/css/home.css">

</head>

<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo" style="font-size:18px; width: 270px">JDBC数据库访问与MVC架构应用</div>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:void(0)">

                    <span class="layui-icon layui-icon-user" style="font-size: 20px">
                    </span>
                    ${currUser.userName}
                </a>
            </li>
            <!--注销按钮-->
            <li class="layui-nav-item"><a href="/user/logout">注销</a></li>
        </ul>
    </div>
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <ul class="layui-nav layui-nav-tree">
                <li class="layui-nav-item ">
                    <a class="javascript:;" href="javascript:;">
                        用户管理<i class="layui-icon layui-icon-down layui-nav-more"></i></a>
                    <dl class="layui-nav-child">
                        <dd class="layui-this">

                        </dd>
                    </dl>
                </li>
            </ul>
        </div>
    </div>
</div>
<div class="welcome" style="position: absolute; left: 300px; top: 300px">
    <div class="content">
        <div class="sub-title">欢迎体验</div>
        <div class="desc">你的用户ID为: ${currUser.userId} </div>
        <div class="desc">你的账号名为: ${currUser.userName} </div>
        <div class="desc">我们对用户密码使用 MD5 算法加密，并且使用了「盐」，请您放心注册 </div>
    </div>
    <div class="img"></div>
</div>
<div class="layui-footer">
    Copyright © zengshen. All Rights Reserved.
</div>
</body>
</html>
