package com.xiangkai.community.model.entity;

import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Event {

    /**
     * 事件类型id
     */
    private final Integer eventTypeId;

    /**
     * 主题
     */
    private final String topic;

    /**
     * 事件发起者用户id
     */
    private final Integer userId;

    /**
     * 事件发生对应的实体类型
     */
    private final Integer entityType;

    /**
     * 事件发生对应的实体id
     */
    private final Integer entityId;

    /**
     * 事件发生对应的目标用户
     */
    private final Integer targetUserId;

    /**
     * 事件发生的时间戳
     */
    private final Long timestamp;

    /**
     * 转载一般数据类型
     */
    private final Map<String, Object> data;

    public Event(Builder builder) {
        this.eventTypeId = builder.eventTypeId;
        this.topic = builder.topic;
        this.userId = builder.userId;
        this.entityType = builder.entityType;
        this.entityId = builder.entityId;
        this.targetUserId = builder.targetUserId;
        this.timestamp = builder.timestamp;
        this.data = builder.data;
    }

    public Integer eventTypeId() {
        return eventTypeId;
    }

    public String topic() {
        return topic;
    }

    public Integer userId() {
        return userId;
    }

    public Integer entityType() {
        return entityType;
    }

    public Integer entityId() {
        return entityId;
    }

    public Integer targetUserId() {
        return targetUserId;
    }

    public Long timestamp() {
        return timestamp;
    }

    public Map<String, Object> data() {
        return data;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType=" + eventTypeId +
                ", topic='" + topic + '\'' +
                ", userId=" + userId +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", targetUserId=" + targetUserId +
                ", timestamp=" + timestamp +
                ", data=" + JSONObject.toJSONString(data) +
                '}';
    }

    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    public static class Builder {
        private Integer eventTypeId;
        private String topic;
        private Integer userId;
        private Integer entityType;
        private Integer entityId;
        private Integer targetUserId;
        private Long timestamp;
        private final Map<String, Object> data;

        public Builder() {
            this.data = new HashMap<>();
        }

        public Builder(Event event) {
            this.eventTypeId = event.eventTypeId;
            this.topic = event.topic;
            this.userId = event.userId;
            this.entityType = event.entityType;
            this.entityId = event.entityId;
            this.targetUserId = event.targetUserId;
            this.timestamp = event.timestamp;
            this.data = event.data;
        }

        public Builder eventTypeId(Integer eventType) {
            this.eventTypeId = eventType;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder entityType(Integer entityType) {
            this.entityType = entityType;
            return this;
        }

        public Builder entityId(Integer entityId) {
            this.entityId = entityId;
            return this;
        }

        public Builder targetUserId(Integer targetUserId) {
            this.targetUserId = targetUserId;
            return this;
        }

        public Builder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder data(String key, Object value) {
            data.put(key, value);
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}
