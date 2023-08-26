package com.jingdong.manager.service;

import java.util.List;

public interface RoleMenuService {
    public List<Long> getRolePermission(Long roleId);

    public List<Long> setPermission(String str, Long roleId);

   public void delete(Long roleId);
    public void deleteByMenuId(Long menuId);

    public void add(Long roleId, Long menuId);
}
