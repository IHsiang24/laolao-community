package com.xiangkai.community.model.entity;

import java.util.Date;

public class Like {

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

    public Like setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Like setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public Like setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public Like setEntityType(Integer entityType) {
        this.entityType = entityType;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Like setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
