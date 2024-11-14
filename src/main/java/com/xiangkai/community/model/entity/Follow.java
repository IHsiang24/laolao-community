package com.xiangkai.community.model.entity;

import java.util.Date;

public class Follow {

    private Integer id;

    private Integer userId;

    private Integer entityType;

    /**
     * 被关注类实体ID
     */
    private Integer entityId;

    private Date timestamp;

    public Integer getId() {
        return id;
    }

    public Follow setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Follow setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public Follow setEntityType(Integer entityType) {
        this.entityType = entityType;
        return this;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public Follow setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Follow setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
