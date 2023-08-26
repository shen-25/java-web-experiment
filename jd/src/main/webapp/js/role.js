layui.use(["form"], function () {
    let form = layui.form;
    let layer = layui.layer;
    form.verify({
        roleName: function (value) {
            if ($.trim(value) === "") {
                return "角色名称不能为空";
            }
        },
    });
})