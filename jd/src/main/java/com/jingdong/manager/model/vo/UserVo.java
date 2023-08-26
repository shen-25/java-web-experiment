package com.jingdong.manager.model.vo;

import com.jingdong.manager.model.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author word
 */

public class UserVo extends User {
    private List<PermissionTreeVo> permissionTreeVoList = new ArrayList<>();

    public List<PermissionTreeVo> getPermissionTreeVoList() {
        return permissionTreeVoList;
    }

    public void setPermissionTreeVoList(List<PermissionTreeVo> permissionTreeVoList) {
        this.permissionTreeVoList = permissionTreeVoList;
    }
}
