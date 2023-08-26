package com.jingdong.manager.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author word
 */
public class PermissionTreeVo implements Serializable {

    private String title;
    private Long id;
    private Boolean spread = true;
    private List<PermissionTreeVo> children = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSpread() {
        return spread;
    }

    public void setSpread(Boolean spread) {
        this.spread = spread;
    }

    public List<PermissionTreeVo> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionTreeVo> children) {
        this.children = children;
    }
}
