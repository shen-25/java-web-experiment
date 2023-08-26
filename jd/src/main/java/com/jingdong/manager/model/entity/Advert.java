package com.jingdong.manager.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * manager_advert
 * @author 
 */
public class Advert implements Serializable {
    private Long id;

    private String name;

    private String advertDesc;

    private Integer status;

    private Integer advertOrder;

    private String picUrl;

    private String url;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdvertDesc() {
        return advertDesc;
    }

    public void setAdvertDesc(String advertDesc) {
        this.advertDesc = advertDesc;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }



    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAdvertOrder() {
        return advertOrder;
    }

    public void setAdvertOrder(Integer advertOrder) {
        this.advertOrder = advertOrder;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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