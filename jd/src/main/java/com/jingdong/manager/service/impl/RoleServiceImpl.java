package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.RoleCommand;
import com.jingdong.manager.command.RoleUserCommand;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Role;
import com.jingdong.manager.model.vo.RoleKeyValueVo;
import com.jingdong.manager.service.RoleMenuService;
import com.jingdong.manager.service.RoleService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author word
 */

public class RoleServiceImpl implements RoleService {




    private RoleMenuService roleMenuService = new RoleMenuServiceImpl();

    private RoleCommand roleCommand = new RoleCommand();
    private RoleUserCommand roleUserCommand = new RoleUserCommand();

    @Override
    public List<RoleKeyValueVo> selectRoleKeyValue() {

        return roleCommand.selectRoleKeyValue();
    }

    @Override
    public Map<String, Object> paging(String roleName, Integer pageNum, Integer pageSize){
//        Page<Role> page = new Page<>(pageNum, pageSize);
//        QueryWrapper<Role> roleWrapper = new QueryWrapper<>();
//        roleWrapper.orderByAsc("role_id");
//
//        if (!StringUtils.isNullOrEmpty(roleName)) {
//            roleWrapper.like("role_name", roleName);
//        }
//        Page<Role> rolePage = roleMapper.selectPage(page, roleWrapper);
//        return rolePage;
        Map<String, Object> stringObjectMap = roleCommand.selectPage(roleName, pageNum, pageSize);
        return stringObjectMap;
    }

    @Override
    public void add(Role role) {

        List<Role> roleList = roleCommand.selectList(role.getRoleName());
        if (!roleList.isEmpty()) {
            throw new BusinessException(BusinessExceptionEnum.ROLE_NAME_CANNOT_BE_THE_SAME);
        }
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        roleCommand.insert(role);
    }

    @Override
    public void edit(Role role) {
        Role oldRole = roleCommand.selectById(role.getRoleId());
        if (oldRole == null) {
            throw new BusinessException(BusinessExceptionEnum.ROLE_NOT_EXISTS);
        }
        if (!oldRole.getRoleName().equals(role.getRoleName())) {
            throw new BusinessException(BusinessExceptionEnum.ROLE_NAME_CANNOT_EDIT);
        }
        oldRole.setUpdateTime(new Date());
        oldRole.setState(role.getState());
        oldRole.setRemark(role.getRemark());
        roleCommand.updateById(oldRole);

    }

    @Override
    public Integer delete(Long roleId) {
        Role role = roleCommand.selectById(roleId);
        if (role == null) {
            throw new BusinessException(BusinessExceptionEnum.ROLE_NOT_EXISTS);
        }
        roleMenuService.delete(roleId);
        roleUserCommand.deleteByRoleId(roleId);
        int cnt = roleCommand.deleteById(roleId);
        return cnt;
    }

}
