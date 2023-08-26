<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="/jd/css/index.css" />
    <link rel="stylesheet" href="/jd/resources/layui/css/layui.css" />
    <script src="/jd/resources/layui/xm-select.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/sweetalert2.all.min.js"></script>


    <title>Document</title>
  </head>
  <style>
    .create-user-main {
     padding: 10px 30px 10px 0px;
      overflow: hidden;
    }
  </style>
  <body>
    <div class="create-user-main">
      <form class="layui-form" lay-filter="userLine">
        <div class="layui-form-item">
          <label class="layui-form-label">用户名</label>
          <div class="layui-input-block">
            <input
              type="text"
              name="userName"
              lay-verify="title"
              autocomplete="off"
              placeholder="请输入用户名"
              class="layui-input"
            />
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">邮箱</label>
          <div class="layui-input-block">
            <input
              type="text"
              name="userEmail"
              lay-verify="email"
              lay-verify="required"
              autocomplete="off"
              placeholder="请输入用户邮箱"
              class="layui-input"
            />
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">手机号</label>
          <div class="layui-input-block">
            <input
              type="text"
              name="mobile"

              lay-verify="required|phone"
              autocomplete="off"
              placeholder="请输入用户手机号"
              class="layui-input"
            />
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">性别</label>
          <div class="layui-input-block">
            <select name="sex" lay-verify="sex" lay-reqtext="请选择性别">
              <option value="-1">请选择性别</option>
              <option value="1">男</option>
              <option value="2">女</option>
            </select>
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">岗位</label>
          <div class="layui-input-block">
            <input
              type="text"
              name="job"

              lay-verify="required"
              autocomplete="off"
              placeholder="请输入岗位"
              class="layui-input"
            />
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">状态</label>
          <div class="layui-input-block">
            <select name="state" lay-filter=""    lay-verify="required">
              <option value="1">在职</option>
              <option value="2">离职</option>
              <option value="3">试用期</option>
            </select>
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">系统角色</label>
          <div class="layui-input-block">
            <div id="multiple"></div>
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">所属部门</label>
          <div class="layui-input-block">
            <select
              name="deptId"
              lay-verify="department"
              lay-verify="required"
              lay-reqtext="请选择所属部门"
            >
              <option value="-1">请选择所属部门</option>
                <c:forEach items="${deptKeyValues}" var="deptKeyValue">
                  <option value="${deptKeyValue.deptId}">${deptKeyValue.deptName}</option>
                </c:forEach>
            </select>
          </div>
        </div>
        <div class="layui-form-item" style="display:none">
          <div
            class="layui-input-block"

          >
            <button
              type="submit"
              class="layui-btn"
              lay-submit=""
              lay-filter="userInput"
            >
              立即提交
            </button>
          </div>
        </div>
      </form>
      <input id="rightInput" style="display: none"/>
    </div>
    <script src="/jd/js/user.js"> </script>
   <script>
   const prefix = "/jd"

     const  multiple = xmSelect.render({
       el: "#multiple",
       name: "roleIds",
       theme: {
         color: "#5fb878",
       },
       data: [],
     });
     $.ajax({
       url: prefix + '/sys/role/role_key_value',
       type: 'get',
       dataType: "json",
       success: function (json){
         if(json.code === 200){
           multiple.update({
             data:  json.data,
             autoRow: true,
           });
         }
       }
     })
     layui.use(["form"], function () {
      let form = layui.form;
      form.on("submit(userInput)",  function (data) {
        let userFormReq = data.field;
        userFormReq.roleIds = multiple.getValue('value');
          $.ajax({
          url: prefix +'/sys/user/add',
          type: 'post',
          data: JSON.stringify(userFormReq),
          contentType: 'application/json',
          dataType: 'json',
          success:  function (json) {
            if (json.code === 200) {
              let index = parent.layer.getFrameIndex(window.name);
              parent.layer.close(index);
              parent.layer.msg("新增成功", {
                icon: 1,
                time: 1300,
                offset: 't',
               })
               $('.layui-laypage-btn', parent.document).click()
            } else {
                parent.layer.msg(json.msg, {icon: 2,
                area: '100px',
                time: 1300,
                offset: 't',
              });
            }
          }
        })
        return false;
      });
     })
  </script>
  </body>
</html>
