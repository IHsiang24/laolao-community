package com.xiangkai.community.model.entity;

import java.util.Date;

public class Comment {

    private Integer Id;

    private Integer userId;

    private Integer entityType;

    private Integer entityId;

    private Integer targetId;

    private String content;

    private Integer status;

    private Date createTime;

    public Integer getId() {
        return Id;
    }

    public Comment setId(Integer id) {
        Id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Comment setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public Comment setEntityType(Integer entityType) {
        this.entityType = entityType;
        return this;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public Comment setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public Comment setTargetId(Integer targetId) {
        this.targetId = targetId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Comment setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public Comment setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Comment setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Comment build() {
        return this;
    }
}
