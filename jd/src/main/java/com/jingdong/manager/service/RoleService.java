package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Role;
import com.jingdong.manager.model.vo.RoleKeyValueVo;

import java.util.List;
import java.util.Map;

/**
 * @author word
 */
public interface RoleService {
    public List<RoleKeyValueVo> selectRoleKeyValue();

    Map<String, Object> paging(String roleName, Integer pageNum, Integer pageSize);

    public void add(Role role);
    public void edit(Role role);

    public Integer delete(Long roleId);
}
