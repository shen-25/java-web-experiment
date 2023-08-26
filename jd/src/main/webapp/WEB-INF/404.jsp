<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>404</title>
</head>
<style>
    body{
        background-color: #fff;
    }
    .content {
        width: 680px;
        height: 300px;
        transform: translate(60%, 50%);


    }
    .image {
        width: 300px;
        height: 300px;
        background: url("/jd/images/404.png")
        no-repeat;
        background-size: 300px;
        justify-content: center;
        float: left;
    }
    .msg {
        text-align: center;
        float: right;
        padding-top: 150px;
        margin-right: 60px;

        justify-content: center;
        font-size: 18px;
    }
</style>
<body>
<div class="content">
    <div class="image"></div>
    <div class="msg">
        <div style="text-align: left; font-size: 23px; padding-bottom: 10px;" >页面不存在</div>
           <div> 抱歉！我们找不到您想访问的页面...</div>
    </div>
</div>
</body>
</html>
