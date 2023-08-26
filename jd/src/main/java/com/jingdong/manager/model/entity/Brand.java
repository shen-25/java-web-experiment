package com.jingdong.manager.model.entity;

import java.util.Date;

/**
 * manager_brand
 * @author 
 */
public class Brand {


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private Long brandId;

    private String brandName;

    private String brandPicUrl;

    private String brandDesc;

    /**
     * 0-显示 1-不显示
     */
    private Integer brandState;

    private Integer brandOrder;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandPicUrl() {
        return brandPicUrl;
    }

    public void setBrandPicUrl(String brandPicUrl) {
        this.brandPicUrl = brandPicUrl;
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }

    public Integer getBrandState() {
        return brandState;
    }

    public void setBrandState(Integer brandState) {
        this.brandState = brandState;
    }

    public Integer getBrandOrder() {
        return brandOrder;
    }

    public void setBrandOrder(Integer brandOrder) {
        this.brandOrder = brandOrder;
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
}