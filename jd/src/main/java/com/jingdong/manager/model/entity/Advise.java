package com.jingdong.manager.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * manager_advise
 * @author 
 */
public class Advise implements Serializable {
    private Long id;

    private String content;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}