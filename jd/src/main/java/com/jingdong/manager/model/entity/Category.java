package com.jingdong.manager.model.entity;

import java.util.Date;

/**
 * manager_category
 * @author 
 */

public class Category  {
    private Long categoryId;

    private String categoryName;

    private Integer categoryOrder;

    private Date createTime;

    private Date updateTime;

    /**
     * 0-启动 1-停用
     */
    private Integer categoryState;

    private static final long serialVersionUID = 1L;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryOrder() {
        return categoryOrder;
    }

    public void setCategoryOrder(Integer categoryOrder) {
        this.categoryOrder = categoryOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCategoryState() {
        return categoryState;
    }

    public void setCategoryState(Integer categoryState) {
        this.categoryState = categoryState;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}