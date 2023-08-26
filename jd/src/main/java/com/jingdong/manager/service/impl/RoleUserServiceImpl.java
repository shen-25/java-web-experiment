package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.*;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Menu;
import com.jingdong.manager.model.entity.Role;
import com.jingdong.manager.model.entity.RoleMenu;
import com.jingdong.manager.model.entity.RoleUser;
import com.jingdong.manager.model.vo.UserMenuTreeVo;
import com.jingdong.manager.service.RoleUserService;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author word
 */
public class RoleUserServiceImpl implements RoleUserService {



    private RoleUserCommand roleUserCommand = new RoleUserCommand();
    private RoleCommand roleCommand = new RoleCommand();
    private RoleMenuCommand roleMenuCommand = new RoleMenuCommand();
    private MenuCommand menuCommand = new MenuCommand();

    @Override

    public void create(List<Long> roleIds, Long userId) {
        if (roleIds.isEmpty()) {
//            throw new BusinessException(BusinessExceptionEnum.NEED_ROLE);
            return ;
        }
        if (userId == null) {
            throw new BusinessException(BusinessExceptionEnum.NEED_USER);
        }
        roleUserCommand.batchInsert(roleIds, userId);
    }

    @Override
    public List<Long> selectRoleIds(Long userId) {
        List<Long> list = new ArrayList<>();
//        QueryWrapper<RoleUser> roleUserQueryWrapper = new QueryWrapper<>();
//        roleUserQueryWrapper.eq("user_id", userId);
        List<RoleUser> roleUserList = roleUserCommand.selectList(userId);
        for (RoleUser roleUser : roleUserList) {
            Role role = roleCommand.selectById(roleUser.getRoleId());
            if(role == null) {
            	break;
            }
            if (role.getState().equals(Constant.MenuState.NORMAL)) {
                list.add(roleUser.getRoleId());
            }
        }
        return list;
    }


    @Override
    public Integer  delete(Long userId) {
        if (userId == null) {
            throw new BusinessException(BusinessExceptionEnum.NEED_USER);
        }
//        QueryWrapper<RoleUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", userId);
        int cnt = roleUserCommand.delete(userId);
        return cnt;
    }

    @Override
    public Integer batchInsert(List<Long> roleIds, Long userId) {
        roleUserCommand.batchInsert(roleIds, userId);
        return  roleIds.size();
    }

    @Override
    public void update(List<Long> roleIds, Long userId){
        this.delete(userId);
        this.create(roleIds, userId);
    }


    @Override
    public Set<UserMenuTreeVo> menuList(Long userId) {
        List<Long> roleList = this.selectRoleIds(userId);
//        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        Set<UserMenuTreeVo> userMenuTreeVoList = new HashSet<>();

        if(roleList.isEmpty()){
//            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_IS_EMPTY);
            return userMenuTreeVoList;
        }
//        queryWrapper.in("role_id", roleList);
        List<RoleMenu> roleMenuList = roleMenuCommand.selectList(roleList);
        Set<Long> menuSet = new HashSet<>();
        for (RoleMenu roleMenu : roleMenuList) {
            menuSet.add(roleMenu.getMenuId());
        }
        List<Menu> menuList = menuCommand.selectByALL();
        for (Long menuId : menuSet) {
            getUserParentDetail(menuId, userMenuTreeVoList, menuList);
        }

        return userMenuTreeVoList;
    }

    @Override
    public Boolean isSuperAdmin(Long userId) {
        List<Long> longs = this.selectRoleIds(userId);
        for (Long roleId : longs) {
            Role role = roleCommand.selectById(roleId);
            if (role.getRoleName().equals("超级管理员")) {
                return true;
            }
        }
        return false;
    }

    private  void  getUserParentDetail(Long menuId, Set<UserMenuTreeVo> userMenuTreeVoList, List<Menu> menuList ){
        if (menuId == null || menuId == 0) {
            return;
        }
        Menu menu  = null;
        for (Menu temp : menuList) {
            if (temp.getMenuId().equals(menuId)) {
                menu = temp;
                break;
            }
        }
        if (menu != null) {
            UserMenuTreeVo userMenuTreeVo = new UserMenuTreeVo();
            userMenuTreeVo.setMenuId(menu.getMenuId());
            userMenuTreeVo.setMenuName(menu.getMenuName());
            userMenuTreeVo.setMenuType(menu.getMenuType());
            userMenuTreeVo.setUrl(menu.getUrl());
            userMenuTreeVo.setParentId(menu.getParentId());
            userMenuTreeVo.setIcon(menu.getIcon());
            userMenuTreeVo.setMenuOrder(menu.getMenuOrder());
            userMenuTreeVoList.add(userMenuTreeVo);
        }
        getUserParentDetail(menu.getParentId(), userMenuTreeVoList, menuList);
    }
}
