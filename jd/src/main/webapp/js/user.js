
layui.use(["form"], function () {
  let form = layui.form;
  let layer = layui.layer;
  form.verify({
    title: function (value) {
      if ($.trim(value) === "") {
        return "用户名不能为空";
      }
    },

    number: function (value) {
      // if ($.trim(value) !== "" && !/^1\d{10}$/.test(value)) {
      //   return "手机号必须为11位";
      // }
    },
    email: function (value) {
      if ($.trim(value) === "") {
        return "邮箱不能为空";
      }
      // if(!/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(value)){
      //    return "请输入正确的邮箱名"
      // }
    },
    sex: function (value) {
      if ($.trim(value) === "-1") {
        return "请选择性别";
      }
    },
    department: function (value) {
      if ($.trim(value) === "-1") {
        return "请选择所属部门";
      }
    }
  });
});
