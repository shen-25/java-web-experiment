layui.use(["form"], function () {
    let form = layui.form;
    form.verify({
        menuName: function (value) {
            if ($.trim(value) === "") {
                return "菜单名称不能为空";
            }
        }
    });

})