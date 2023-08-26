package com.jingdong.manager.service.impl;


import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.command.MenuCommand;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Menu;
import com.jingdong.manager.model.request.MenuSelectReq;
import com.jingdong.manager.model.vo.PermissionTreeVo;
import com.jingdong.manager.service.MenuService;
import com.jingdong.manager.service.RoleMenuService;


import java.util.*;


public class MenuServiceImpl implements MenuService {


    private MenuCommand menuCommand = new MenuCommand();

    private RoleMenuService roleMenuService = new RoleMenuServiceImpl();

    @Override
    public  List<PermissionTreeVo> permissionTree(Long menuId) {
        List<PermissionTreeVo> permissionTreeVoList = new ArrayList<>();
        this.permissionTree(permissionTreeVoList, menuId);
        return permissionTreeVoList;

    }

    @Override
    public List<PermissionTreeVo> permissionTreeForAll() {
        List<PermissionTreeVo> permissionTreeVoList = new ArrayList<>();
        this.permissionTree(permissionTreeVoList, 0L);
        return permissionTreeVoList;
    }

    @Override
    public Menu selectMenuByMenuName(String menuName) {
        if (StringUtils.isNullOrEmpty(menuName)) {
            throw new BusinessException(BusinessExceptionEnum.NEED_MENU_NAME);
        }

        Menu menu = menuCommand.selectByMenuName(menuName);
        return menu;
    }

    @Override
    public  Map<String, Object> paging(String menuName, String url, String menuState, Integer pageNum, Integer pageSize) {
        Map<String, Object> stringObjectMap = menuCommand.selectPage(menuName, url, menuState, pageNum, pageSize);
        return stringObjectMap;
    }

    @Override
    public void add(Menu menu) {
        Menu menu1 = menuCommand.selectOne(menu.getMenuName(), Constant.MenuType.MENU);
        if (menu1 != null) {
            throw new BusinessException(BusinessExceptionEnum.MENU_NAME_CANNOT_BE_THE_SAME);
        }

        Menu menuFind = menuCommand.selectByUrl(menu.getUrl());
        if (menuFind != null) {
            throw new BusinessException(BusinessExceptionEnum.MENU_URL_EXISTS);
        }
        menu.setCreateTime(new Date());
        menuCommand.insert(menu);

    }

    @Override
    public void edit(Menu menu) {
        Menu oldMenu = menuCommand.selectById(menu.getMenuId());
        Menu menu1 = menuCommand.selectOne( menu.getMenuName(),Constant.MenuType.MENU);
        if (menu1 != null && !menu1.getMenuId().equals(oldMenu.getMenuId())) {
            throw new BusinessException(BusinessExceptionEnum.MENU_NAME_CANNOT_BE_THE_SAME);
        }

        Menu menu2 = menuCommand.selectByUrl(menu.getUrl());
        if (menu2 != null && !menu2.getMenuId().equals(oldMenu.getMenuId())) {
            throw new BusinessException(BusinessExceptionEnum.MENU_URL_EXISTS);
        }
        oldMenu.setUpdateTime(new Date());
        oldMenu.setParentId(menu.getParentId());
        oldMenu.setMenuName(menu.getMenuName());
        oldMenu.setUrl(menu.getUrl());
        oldMenu.setIcon(menu.getIcon());
        oldMenu.setMenuOrder(menu.getMenuOrder());
        oldMenu.setMenuState(menu.getMenuState());
        oldMenu.setMenuType(menu.getMenuType());
        menuCommand.updateById(oldMenu);
    }

    @Override
    public void delete(String menuId) {
        Long menuIdTemp = Long.parseLong(menuId);
        List<PermissionTreeVo> permissionTreeVoList = new ArrayList<>();
        this.permissionTree(permissionTreeVoList, menuIdTemp);
        List<Long> menuDeleteIds = new ArrayList<>();
        if (permissionTreeVoList.isEmpty()) {
            roleMenuService.deleteByMenuId(menuIdTemp);
        } else{
            getChildrenIds(permissionTreeVoList, menuDeleteIds);
        }
         menuDeleteIds.add(menuIdTemp);
        menuCommand.batchDeleteMenu(menuDeleteIds);
    }

    private void getChildrenIds( List<PermissionTreeVo> permissionTreeVoList, List<Long> menuParentIds) {
        if(permissionTreeVoList.isEmpty()){
            return;
        }
        for (PermissionTreeVo permissionTreeVo : permissionTreeVoList) {
            menuParentIds.add(permissionTreeVo.getId());
            if(permissionTreeVo.getChildren().isEmpty()){
                roleMenuService.deleteByMenuId(permissionTreeVo.getId());
            }
            getChildrenIds(permissionTreeVo.getChildren(), menuParentIds);
        }
    }

    @Override
    public List<MenuSelectReq> menuNameSelect() {
        List<Menu> menuList = menuCommand.selectListByMenuType(Constant.MenuType.MENU);
        List<MenuSelectReq> menuSelectReqList = new ArrayList<>();
        for (Menu menu : menuList) {
            MenuSelectReq menuSelectReq = new MenuSelectReq();
            menuSelectReq.setMenuId(menu.getMenuId());
            menuSelectReq.setMenuName(menu.getMenuName());
            if (menu.getParentId() == 0) {
                menuSelectReq.setGrade("一级");
            } else {
                menuSelectReq.setGrade("二级");
            }
            menuSelectReqList.add(menuSelectReq);
        }
         Collections.sort(menuSelectReqList, (s1, s2) -> {
            return s1.getGrade().toString().compareTo(s2.getGrade().toString());
        });
        return menuSelectReqList;
    }

    private void permissionTree(List<PermissionTreeVo> permissionTreeVoList, Long parentId) {
        List<PermissionTreeVo> permissionTreeVos = menuCommand.selectPermissionByParentId(parentId);
  
        for (PermissionTreeVo permissionTreeVo : permissionTreeVos) {
            PermissionTreeVo permissionTreeVoTemp = new PermissionTreeVo();
            permissionTreeVoTemp.setId(permissionTreeVo.getId());
            permissionTreeVoTemp.setTitle(permissionTreeVo.getTitle());
            permissionTreeVoTemp.setSpread(permissionTreeVo.getSpread());
            permissionTreeVoTemp.setChildren(new ArrayList<>(permissionTreeVo.getChildren()));
            permissionTreeVoList.add(permissionTreeVoTemp);

            permissionTree(permissionTreeVoTemp.getChildren(), permissionTreeVo.getId());
//         
          
        }
    }
}
