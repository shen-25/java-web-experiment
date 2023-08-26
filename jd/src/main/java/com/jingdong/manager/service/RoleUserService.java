package com.jingdong.manager.service;

import com.jingdong.manager.model.vo.UserMenuTreeVo;

import java.util.List;
import java.util.Set;

/**
 * @author word
 */
public interface RoleUserService {
    void create(List<Long> roleIds, Long userId);

    public List<Long> selectRoleIds(Long userId);

    Integer  delete(Long userId);

    public Integer batchInsert(List<Long> roleIds, Long userId);

    void update(List<Long> roleIds, Long userId);


    public Set<UserMenuTreeVo> menuList(Long userId);

    public Boolean isSuperAdmin(Long userId);
}
