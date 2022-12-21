layui.use(["form"], function () {
    let form = layui.form;
    form.verify({
        categoryName: function (value) {
            if ($.trim(value) === "") {
                return "分类名称不能为空";
            }
        },
        categoryOrder: function (value) {

            if ($.trim(value) === "") {
                return "分类排序不能为空";
            }
            if(!Number(value)){
                return "分类排序类型必须为整数";
            }
        }
    });

})