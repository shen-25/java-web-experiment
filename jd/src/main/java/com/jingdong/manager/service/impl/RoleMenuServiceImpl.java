package com.jingdong.manager.service.impl;


import com.alibaba.fastjson.JSON;
import com.jingdong.manager.command.RoleCommand;
import com.jingdong.manager.command.RoleMenuCommand;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;

import com.jingdong.manager.model.entity.Role;
import com.jingdong.manager.model.entity.RoleMenu;
import com.jingdong.manager.service.RoleMenuService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author word
 */

public class RoleMenuServiceImpl implements RoleMenuService {


    private RoleMenuCommand roleMenuCommand = new RoleMenuCommand();
    private RoleCommand roleCommand = new RoleCommand();

    @Override
    public List<Long> getRolePermission(Long roleId) {
        List<RoleMenu> roleMenuList = roleMenuCommand.selectListByRoleId(roleId);
        List<Long> menuIds = new ArrayList<>();
        for (RoleMenu roleMenu : roleMenuList) {
            menuIds.add(roleMenu.getMenuId());
        }
        return menuIds;
    }

    @Override
    public List<Long> setPermission(String str, Long roleId) {

        Object parse = JSON.parse(str);
        List<Map<String, Object>> permission = (List<Map<String, Object>>) parse;
        if (permission.isEmpty()) {
            this.delete(roleId);
            return null;
        }
        List<Long> res = new ArrayList<>();
        getPermissionId(permission, res);
        this.delete(roleId);
        roleMenuCommand.batchInsert(res, roleId);
        Role role = roleCommand.selectById(roleId);
        role.setUpdateTime(new Date());
        roleCommand.updateById(role);
        return res;
    }

    @Override
    public void delete(Long roleId) {
        if (roleId == null) {
            throw new BusinessException(BusinessExceptionEnum.NEED_ROLE);
        }

        roleMenuCommand.deleteByRoleId(roleId);

    }

    @Override
    public void deleteByMenuId(Long menuId) {
        if (menuId == null) {
            throw new BusinessException(BusinessExceptionEnum.NEED_ROLE);
        }
        List<RoleMenu> roleMenus = roleMenuCommand.selectListByMenuId(menuId);
        if(roleMenus.isEmpty()){
            return;
        }
        roleMenuCommand.deleteByMenuId(menuId);
    }

    @Override
    public void add(Long roleId, Long menuId) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setMenuId(menuId);
        roleMenu.setRoleId(roleId);
        roleMenuCommand.insert(roleMenu);
    }


    private void getPermissionId(List<Map<String, Object>> permissionTreeVos, List<Long> res) {
        if (permissionTreeVos.isEmpty()) {
            return;
        }

        for (Map<String, Object> object : permissionTreeVos) {
            List<Map<String, Object>> Children = (List<Map<String, Object>>) object.get("children");
            if (Children.isEmpty()) {
               res.add(((Integer) object.get("id")).longValue());
            }
            getPermissionId(Children, res);
        }
    }
}
