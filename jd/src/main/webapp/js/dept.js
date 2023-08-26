layui.use(["form"], function () {
    let form = layui.form;
    form.verify({
        deptName: function (value) {
            if ($.trim(value) === "") {
                return "部门名称不能为空";
            }
        }
    });

})