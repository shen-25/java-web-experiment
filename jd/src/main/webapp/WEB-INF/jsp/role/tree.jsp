<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>设置权限</title>
    <meta name="renderer" content="webkit" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="/jd/resources/layui/css/layui.css" />
    <link rel="stylesheet" href="/jd/css/role.css" />
    <script src="/jd/resources/layui/layui.js"></script>
    <script src="/jd/resources/jquery.3.3.1.min.js"></script>
  </head>
  <body>
    <div class="tree-main">
      <div class="role-tips">
        <div class="role-name">角色名称:</div>
        <div class="role-name-value" id="treeRoleName"></div>
      </div>
      <div class="power">
        <div class="role-select-left">选择权限</div>
        <div id="tree" class="tree-more role-select-right"></div>
      </div>
    </div>
    <div style="display: none">
      <input
              type="text"
              name="treeRoleId"
              id="treeRoleId"
      />
      <button type="button" class="layui-btn layui-btn-sm" id="getData" lay-demo="getChecked">获取选中节点数据</button>
    </div>
  </body>
  <script>
    layui.use(["tree", "util"], async function () {
      let tree = layui.tree;
      let layer = layui.layer;
      let util = layui.util;
      let data = null;
     await $.ajax({
        url: "/jd/sys/menu/permission/tree",
        type: 'get',
        dataType: 'json',
        success:  function (json) {
          if(json.code === 200)
            data = json.data;
        }
      });
      tree.render({
        elem: "#tree",
        data: data,
        showCheckbox: true, //是否显示复选框
        id: "permissionTree",
        isJump: true, //是否允许点击节点时弹出新窗口跳转
      });

      $(document).ready(function () {
        $.ajax({
          url: '/jd/sys/role_menu/role/permission',
          type: 'get',
          data: {
            "roleId": $('#treeRoleId').val()
          },
          dataType: 'json',
          success: function (json) {
            if (json.code === 200) {
              tree.setChecked('permissionTree', json.data);
            }
          }
        });
      });

      util.event('lay-demo', {
         getChecked: function(othis){
          let checkedData = tree.getChecked('permissionTree'); //获取选中节点的数据
          // layer.alert(JSON.stringify(checkedData), {shade:0});
          $.ajax({
             url: "/jd/sys/role_menu/role/setPermission",
             type: 'post',
             data: {
               'data': JSON.stringify(checkedData),
               'roleId' :  $('#treeRoleId').val()
             },
             dataType: 'json',
            success: function (json) {
              let index = parent.layer.getFrameIndex(window.name);
              parent.layer.close(index);
              if (json.code === 200) {
                parent.layer.msg("编辑成功", {
                  icon: 1,
                  area: '100px',
                  time: 1300,
                  offset: 't',
                })
              } else {
                parent.layer.msg(json.msg, {icon: 2,
                  time: 1300,
                  offset: 't',
                });
              }
            }
          })
        }
      });
    });
  </script>
</html>
