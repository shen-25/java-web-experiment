<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/11/20
  Time: 上午 10:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>首页</title>
    <style>
        body{
            height: 100%;
            background-color: #fff;

        }
        #brower {
            display: flex;
            justify-content: center;
        }
        #brower .brower__item {
            width: 46.5%;
            height: 186px;
            margin-top: 22px;
            display: flex;
            flex-direction: column;
            justify-items: center;
            border: 1px solid #dcdfe6;
            margin-right: 20px;
        }
        #brower .brower__item .brower_item_title {
            height: 23%;
            background-color: #f2f6fc;
            color: #606266;
            padding: 10px 0px 10px 20px;

            box-sizing: border-box;

            font-weight: 700;
        }
        .brower_item_title_index {
            height: 60%;
            display: flex;
            flex-direction: row;
            justify-items: center;
            justify-content: center;
            align-items: center;
        }
        #brower .brower__item .brower_item_title_item {
            width: 25%;
            height: 22%;
            display: flex;
            flex-direction: column;
        }
        #brower .brower__item .brower_item_title_item .item_number {
            color: rgb(245, 108, 126);
            font-size: 24px;
            text-align: center;
            margin-bottom: 8px;
        }
        #brower .brower__item .brower_item_title_item .item_name {
            font-size: 16px;
            text-align: center;
            color: #606266;
        }
        .charts{
            display: flex;
            flex-direction: row;
            justify-content: center;
            margin-top: 30px;
        }
        .charts .charts__item{

            display: flex;
            width: 21.5%;
            height: 100px;
            border: 1px solid #dcdfe6;
            align-items: center;
            padding-left: 10px;
            margin-right: 20px;

        }
        .charts .item_image{
            width: 60px;
            height: 60px;
        }
        .charts  .item_content{
            margin-left: 18px;
            text-align: left;
        }
        .charts  .item_content .item_title_total{
            font-size: 16px;
            color: #909399;
            margin-bottom: 8px;
        }
        .charts .item_content .item_title_number{
            font-size: 19px;
            color: #606266;
        }
    </style>
</head>
<body>
<div class="charts">

    <div class="charts__item">
        <div> <img src="/jd/images/order.png" alt=""   class="item_image"></div>

        <div class="item_content">
            <div class="item_title_total">今日订单总数</div>
            <div class="item_title_number">
                ${todayOrderTotal}
            </div>
        </div>
    </div>

    <div class="charts__item">
        <div> <img src="/jd/images/today-sales.png" alt=""   class="item_image"></div>

        <div class="item_content">
            <div class="item_title_total">今日销售总额
            </div>
            <div class="item_title_number">
                ￥<fmt:formatNumber value=" ${todayOrderValues / 100}" pattern="0.00"></fmt:formatNumber>
            </div>
        </div>
    </div>

    <div class="charts__item">
        <div> <img src="/jd/images/yesterday-sales.png" alt=""   class="item_image"></div>

        <div class="item_content">
            <div class="item_title_total">昨日销售总额
            </div>
            <div class="item_title_number">
                ￥<fmt:formatNumber value="${yesterdayOrderValues / 100}" pattern="0.00"></fmt:formatNumber></div>
        </div>
    </div>
    <div class="charts__item">
        <div> <img src="/jd/images/views.png" alt=""   class="item_image"></div>

        <div class="item_content">
            <div class="item_title_total">用户浏览总量</div>
            <div class="item_title_number">
                <fmt:formatNumber value="48956815" pattern="0,000"></fmt:formatNumber>
            </div>
        </div>
    </div>


</div>
<div id="brower">
    <div class="brower__item">
        <div class="brower_item_title">商品总览</div>
        <div class="brower_item_title_index">
            <div class="brower_item_title_item">
                <div class="item_number">${upState}</div>
                <div class="item_name">已上架</div>
            </div>
            <div class="brower_item_title_item">
                <div class="item_number">${downState}</div>
                <div class="item_name">已下架</div>
            </div>
            <div class="brower_item_title_item">
                <div class="item_number">${tightProduct}</div>
                <div class="item_name">库存紧张</div>
            </div>
            <div class="brower_item_title_item">
                <div class="item_number">${allProduct}</div>
                <div class="item_name">全部商品数量</div>
            </div>
        </div>
    </div>
    <div class="brower__item">
        <div class="brower_item_title">用户总览</div>
        <div class="brower_item_title_index">
            <div class="brower_item_title_item">
                <div class="item_number">143</div>
                <div class="item_name">今日新增</div>
            </div>
            <div class="brower_item_title_item">
                <div class="item_number">1324</div>
                <div class="item_name">昨日新增</div>
            </div>
            <div class="brower_item_title_item">
                <div class="item_number">33134</div>
                <div class="item_name">本月新增</div>
            </div>
            <div class="brower_item_title_item">
                <div class="item_number">3243241</div>
                <div class="item_name">会员总数
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>

