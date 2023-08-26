package com.jingdong.manager.model.entity;


/**
 * @author word
 */
public class RoleMenu {
    private Long rmId;
    private Long roleId;
    private Long menuId;

    public Long getRmId() {
        return rmId;
    }

    public void setRmId(Long rmId) {
        this.rmId = rmId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
