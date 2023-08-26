<%--
  Created by IntelliJ IDEA.
  User: word
  Date: 2022/11/18
  Time: 下午 03:02
  To change this template use File | Settings | File Templates.
--%>
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
    <link rel="stylesheet" href="/jd/css/product.css"/>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script src="/jd/resources/sweetalert2.all.min.js"></script>
    <title>商品管理</title>
</head>
<body>
<div class="query-form">
    <form class="layui-form">

        <div class="layui-inline">
            <label class="layui-form-label inline-name">商品名称</label>
            <div class="layui-input-inline">
                <input
                        type="text"
                        name="productName"
                        placeholder="请输入商品名称"
                        lay-verify="text"
                        autocomplete="off"
                        class="layui-input inline-input"
                />
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label inline-name">商品类型</label>
            <div class="layui-input-inline">
                <select name="categoryId" lay-verify="">
                    <option value="-1">全部</option>
                    <c:forEach items="${categoryKeyValueVos}" var="categoryKeyValueVo">
                        <option value="${categoryKeyValueVo.categoryId}">${categoryKeyValueVo.categoryName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label inline-name">商品品牌</label>
            <div class="layui-input-inline">
                <select name="brandId" lay-verify="">
                    <option value="-1">全部</option>
                    <c:forEach items="${brandKeyValueVos}" var="brandKeyValue">
                        <option value="${brandKeyValue.brandId}">${brandKeyValue.brandName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-inline query-from-style-margin-top">
            <label class="layui-form-label inline-name">上架状态</label>
            <div class="layui-input-inline">
                <select name="productState" lay-verify="">

                    <option value="-1">全部</option>
                    <option value="0">上架</option>

                    <option value="1">下架</option>
                </select>
            </div>
        </div>

        <div class="layui-inline query-from-style-margin-top">
            <div class="layui-input-block inline-input inline-style">
                <button class="layui-btn" lay-submit
                        lay-filter="productQueryFilter">查询
                </button>
                <button type="reset" class="layui-btn layui-btn-primary">
                    重置
                </button>
            </div>
        </div>
    </form>
</div>

<div class="layui-btn-container">
    <c:forEach items="${buttons}" var="btn">
        <c:if test="${btn.menuName == '新增'}">
    <button
            type="button"
            class="layui-btn layui-btn-sm layui-btn-normal create-button"
            id="create-product"
    >
        ${btn.menuName}
    </button>
    </c:if>
    </c:forEach>
</div>

<div class="base-table">
    <table id="productTable" lay-filter="productOpera" class="productTable"></table>

    <script type="text/html" id="productBar">
<c:forEach items="${buttons}" var="btn">
    <c:if test="${btn.menuName == '编辑'}">
        <a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="edit"> ${btn.menuName}</a>
    </c:if>
    <c:if test="${btn.menuName == '删除'}">
        <a class="layui-btn layui-btn-danger layui-btn-xs"
           lay-event="del"
        > ${btn.menuName}</a >
    </c:if>
</c:forEach>
    </script>

</div>
</div>

<div id="productDialog" style="display: none; padding: 18px 35px 60px 30px">

    <form class="layui-form product-form" lay-filter="createProductFilter">
        <div style="display: none">
            <label>商品ID</label>
            <div>
                <input
                        type="text"
                        name="productId"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">商品名称</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="productName"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入商品名称"
                        class="layui-input form-productName"
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label inline-name">商品分类</label>
            <div class="layui-input-inline">
                <select name="categoryId"  lay-verify="required">
                    <option value="">【请选择商品类型】</option>
                    <c:forEach items="${categoryKeyValueVos}" var="categoryKeyValueVo">
                        <option value="${categoryKeyValueVo.categoryId}">${categoryKeyValueVo.categoryName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label inline-name">商品品牌</label>
            <div class="layui-input-inline">
                <select name="brandId" lay-verify="required">
                    <option value="">请选择商品品牌</option>
                    <c:forEach items="${brandKeyValueVos}" var="brandKeyValue">
                        <option value="${brandKeyValue.brandId}">${brandKeyValue.brandName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">商品单价</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="productPrice"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入商品单价(元)"
                        class="layui-input"
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">商品库存</label>
            <div class="layui-input-block">
                <input
                        type="number"
                        name="productStock"
                        lay-verify="required"
                        autocomplete="off"
                        placeholder="请输入商品库存"
                        class="layui-input "
                />
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">商品图片</label>
            <input id="productPicUrl" name="productPicUrl" style="display: none" ;>
            <button type="button" class="layui-btn" id="upload-img-btn">
                <i class="layui-icon"></i>点击上传
            </button>
            <div class="layui-upload" style="margin-left: 110px">
                <div class="layui-upload-list div-upload-img-parent">
                    <img class="layui-upload-img" id="img-show"/>
                </div>
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">商品描述</label>
            <div class="layui-input-block">
            <textarea
                    name="productDesc"

                    placeholder="请输入商品描述"
                    class="layui-textarea"
                    autocomplete="off"
                    id="formDesc"
            ></textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">商品排序</label>
            <div class="layui-input-block">
                <input
                        type="text"
                        name="productOrder"

                        autocomplete="off"
                        placeholder="请输入商品排序"
                        class="layui-input "
                />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">上架状态</label>
            <div class="layui-input-block form-productState">
                <input
                        type="radio"
                        name="productState"
                        value="0"
                        title="上架"
                        checked="true"
                />
                <input
                        type="radio"
                        name="productState"
                        value="1"
                        class="form-productState"
                        title="下架"
                />
            </div>
        </div>

        <div class="layui-inline">
            <div class="layui-input-block inline-input inline-style product-submit-btn-style">
                <button type="button" class="layui-btn submit-btn-style mr20" lay-submit lay-filter="product-add-btn"
                        style="display:none;" id="product-add-submit">立即提交
                </button>
                <button type="button" class="layui-btn submit-btn-style mr20" lay-submit lay-filter="product-edit-btn"
                        style="display:none;" id="product-edit-submit">立即提交
                </button>
                <button type="submit" class="layui-btn layui-btn-primary" id="btn-close">
                    关闭
                </button>
                <button type="reset" id="btn-reset" style="display: none">
                    重置
                </button>
            </div>
        </div>
    </form>
</div>
</body>
<script>

    layui.use(['table', 'form', 'upload', 'element'], function () {


        const table = layui.table;
        const layer = layui.layer;
        const form = layui.form;
        const upload = layui.upload;
        const element = layui.element;
        const  categoryKeyValueStr = '<%=(String) request.getAttribute("categoryKeyValueStr")%>';
        const categoryKeyValueJSON = (JSON.parse(categoryKeyValueStr));
        const  brandKeyValueStr = '<%=(String) request.getAttribute("brandKeyValueStr")%>';
        let brandKeyValueJSON = (JSON.parse(brandKeyValueStr));

        layui.use("form", function () {
            let form = layui.form;
            form.on("submit(productQueryFilter)", function (data) {
                let screen = data.field;
                console.log(JSON.stringify(screen));
                table.reload('productTable', {
                    url: '/jd/store/product/list?productName=' + screen.productName + '&categoryId=' + screen.categoryId + '&brandId='
                        + screen.brandId + '&productState=' + screen.productState,
                    page: {
                        curr: 1
                    }
                });
                return false;
            });
        });
        table.render({
            elem: "#productTable",
            height: 485,
            url: '/jd/store/product/list',
            parseData: function (res) {
                return {
                    "code": 0,
                    "msg": res.msg,
                    "count": res.data.total,
                    "data": res.data.records
                };
            },
            page: true,
            id: "productTable",
            cols: [
                [
                    {field: "productId", title: "商品ID", width: 75},
                    {field: "productName", title: "商品名称", width: 170},
                    {
                        field: "productPicUrl", title: "图片", width: 100, templet: function (d) {
                            let productPicUrl = String(d.productPicUrl);
                            return "<a href=" + productPicUrl + " target='_blank'><img src=" + productPicUrl + " height='26'></a></div>";
                        }
                    },


                    {field: "categoryId", title: "类型", width: 170, templet:function (d) {
                            let categoryId = (d.categoryId);
                            for (let i = 0; i < categoryKeyValueJSON.length; i++) {
                                if(categoryKeyValueJSON[i].categoryId == categoryId){
                                    return categoryKeyValueJSON[i].categoryName;
                                }
                            }
                            return "";
                        }
                    },
                    {field: "brandId", title: "品牌", width: 125, templet: function(d){
                            let brandId = (d.brandId);
                            for (let i = 0; i < brandKeyValueJSON.length; i++) {
                                if(brandKeyValueJSON[i].brandId == brandId){
                                    return brandKeyValueJSON[i].brandName;
                                }
                            }
                            return "";
                    }},
                    {field: "productPrice", title: "单价(元)", width: 90, templet: function (d) {
                            return d.productPrice / 1000 ;
                        }},
                    {field: "productStock", title: "库存", width: 110},

                    {
                        field: "productState", title: "状态", width: 80, templet: function (d) {
                            let state = (d.productState);
                            if (state == 0) {
                                return '<span class="layui-btn layui-btn-normal layui-btn-xs">上架</span>';
                            } else {
                                return '<span class="layui-btn layui-btn-danger layui-btn-xs">下架</span>';
                            }
                        }
                    },
                    {field: "productDesc", title: "描述", width: 200},
                    {field: "productOrder", title: "排序", width: 100},
                    {field: "createTime", title: "创建时间", width: 188,  templet: "#createTimeTpl"},
                    {field: "updateTime", title: "更新时间", width: 188,  templet: "#updateTimeTpl"},
                    {
                        fixed: "right",
                        title: "操作",
                        width: 118,
                        toolbar: "#productBar",
                    },
                ],
            ],
        });

        //常规使用 - 普通图片上传
        var uploadInst = upload.render({
            elem: '#upload-img-btn'
            , url: '/jd/store/product/upload/image'
            , before: function (obj) {
                obj.preview(function (index, file, result) {
                    $('#img-show').attr('src', result);
                });

                element.progress('demo', '0%');
                layer.msg('上传中', {icon: 16, time: 0});
            }
            , done: function (res) {
                if (res.code > 0) {
                    return layer.msg('上传失败');
                }
                $("#productPicUrl").val(res.msg);

            }
            //进度条
            , progress: function (n, elem, e) {
                element.progress('demo', n + '%');
                if (n == 100) {
                    layer.msg('上传完毕', {icon: 1});
                }
            }
        });

        table.on("tool(productOpera)", function (obj) {
                const data = obj.data;
                if (obj.event === "del") {
                    layer.confirm("你确定要删除ID[" + data.productId + "]的商品吗？", {icon: 3},
                        function (index) {
                            layer.close(index);
                            $.ajax({
                                url: "/jd/store/product/delete",
                                data: {"productId": data.productId},
                                type: 'post',
                                dataType: 'json',
                                success: function (json) {
                                    if (json.code === 200) {
                                        $(".layui-laypage-btn").click();
                                        layer.msg("删除成功", {
                                            icon: 1,
                                            time: 1300,
                                            offset: 't',
                                        })
                                    } else {
                                        layer.msg(json.msg, {
                                            icon: 2,
                                            anim: 6,
                                            time: 1300,
                                            offset: 't',
                                        });
                                    }
                                }
                            });
                        });
                } else if (obj.event === "edit") {
                    layer.open({
                        title: "编辑商品",
                        type: 1,
                        area: ["100%", "100%"],
                        content: $("#productDialog"),
                        success: function (layero, index) {
                            $("#product-edit-submit").show();
                            $("#product-add-submit").hide();
                            $("#img-show").attr("src", data.productPicUrl);
                            form.val('createProductFilter', {
                                "productId": data.productId,
                                "productName": data.productName,
                                "productPicUrl": data.productPicUrl,
                                "productDesc": data.productDesc,
                                "brandId": data.brandId,

                                "categoryId": data.categoryId,
                                "productPrice": data.productPrice / 1000,


                                "productStock": data.productStock,
                                "productState": data.productState,

                                "createTime": data.createTime,
                                "updateTime": data.updateTime,

                                "productOrder": data.productOrder,
                            });
                        },

                        end: function (index, layero) {
                            $("#productDialog").hide();
                            layer.close(index);
                            form.render();
                            $("#product-edit-submit").hide();

                        }

                    });
                }
            }
        );

        $("#create-product").click(function () {
            layer.open({
                title: "新增商品",
                type: 1,
                area: ["100%", "100%"],
                content: $("#productDialog"),
                success: function (layero, index) {
                    $("#btn-reset").click();
                    $("#product-edit-submit").hide();
                    $("#product-add-submit").show();
                    $("#img-show").removeAttr("src");
                },
                end: function (index, layero) {
                    $("#productDialog").hide();
                    layer.close(index);
                    form.render();
                    $("#product-add-submit").hide();

                }

            });
        });
        form.on('submit(product-add-btn)', function (data) {
            const dataForm =  data.field;
            delete dataForm.file;
            console.log(JSON.stringify(dataForm));
            $.ajax({
                url: "/jd/store/product/add",
                type: 'post',
                data: JSON.stringify(dataForm),
                contentType: 'application/json',
                dataType: 'json',
                success: function (json) {
                    if (json.code === 200) {
                        layui.layer.closeAll();
                        layer.msg("新增成功", {
                            icon: 1,
                            time: 1300,
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
            });
            return false;
        });

        form.on('submit(product-edit-btn)', function (data) {
            const dataForm =  data.field;
            delete dataForm.file;
            console.log(JSON.stringify(dataForm));
            $.ajax({
                url: '/jd/store/product/edit',
                type: 'post',
                data: JSON.stringify(dataForm),
                contentType: 'application/json',
                dataType: 'json',
                success: function (json) {
                    if (json.code === 200) {
                        layui.layer.closeAll();
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
        $("#btn-close").click(function () {
            layer.closeAll();

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
