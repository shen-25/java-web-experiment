package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Menu;
import com.jingdong.manager.model.request.MenuSelectReq;
import com.jingdong.manager.model.vo.PermissionTreeVo;


import java.util.List;
import java.util.Map;

/**
 * @author word
 */
public interface MenuService {

    public List<PermissionTreeVo> permissionTree(Long menuId);

    public List<PermissionTreeVo> permissionTreeForAll();

    public Menu selectMenuByMenuName(String menuName);

    public Map<String, Object> paging(String menuName, String url, String menuState, Integer pageNum, Integer pageSize);

    public void add(Menu menu);

    public void edit(Menu menu);
    public void delete(String menuId);

    public List<MenuSelectReq> menuNameSelect();
}
