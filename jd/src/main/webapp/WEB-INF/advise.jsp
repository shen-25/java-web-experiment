
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>系统通知</title>
    <link rel="stylesheet" href="/jd/resources/layui/css/layui.css" />
</head>
<style>
    body{
        height: 100%;
        background-color: white;
        width: 100%;
    }

    .layui-table-view {
        margin: 0px;
    }
    .layui-elem-quote {
        margin-bottom: 0px;
        padding: 12px;
        line-height: 1.6;
        border-left: 5px solid #5fb878;
        border-radius: 0 2px 2px 0;
        background-color: #fafafa;
    }
</style>
<body>
<div class="layui-row">
    <blockquote class="layui-elem-quote">
        <h2>系统通知</h2>
    </blockquote>
    <table id="grdNoticeList" lay-filter="grdNoticeList"></table>
</div>

<script src="/jd/resources/layui/layui.js"></script>

<script>
    layui.table.render({
        elem: "#grdNoticeList",
        id: "grdNoticeList",
        url: '/jd/sys/advise/list',
        parseData: function (res) {
            return {
                "code": 0,
                "msg": res.msg,
                "data": res.data
            };
        },
        page: false,
        cols: [
            [
                {
                    field: "id",
                    title: "序号",
                    width: "10%",
                    style: "height:50px",
                    type: "numbers",
                },
                {
                    field: "createTime",
                    title: "通知时间",
                    width: "20%",
                    templet: "#createTimeTpl"
                },
                { field: "content", title: "通知内容", width: "70%" },
            ],
        ],
    });
</script>
<script src="/jd/js/dateConversion.js"></script>
<script type="text/html" id="createTimeTpl">
    {{ dateFormat( d.createTime) }}
</script>
</body>
</html>
