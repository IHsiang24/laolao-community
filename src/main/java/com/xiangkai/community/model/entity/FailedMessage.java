package com.xiangkai.community.model.entity;

import lombok.Getter;

import java.util.Date;

@Getter
public class FailedMessage {

    private final Integer id;

    private final String topic;

    private final Integer partition;

    private final String key;

    private final String value;

    private final String message;

    private final String failureReason;

    private final Date createTime;

    public FailedMessage(Builder builder) {
        this.id = builder.id;
        this.topic = builder.topic;
        this.partition = builder.partition;
        this.key = builder.key;
        this.value = builder.value;
        this.message = builder.message;
        this.failureReason = builder.failureReason;
        this.createTime = builder.createTime;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public String toString() {
        return "FailedMessage [id=" + id + ", topic=" + topic + ", partition=" + partition + ", key="
                + key + ", value=" + value + ", failureReason=" + failureReason + ", createTime=" + createTime;
    }

    public static class Builder {
        private Integer id;

        private String topic;

        private Integer partition;

        private String key;

        private String value;

        private String message;

        private String failureReason;

        private Date createTime;

        public Builder() {
        }

        public Builder(FailedMessage failedMessage) {
            this.id = failedMessage.id;
            this.topic = failedMessage.topic;
            this.partition = failedMessage.partition;
            this.key = failedMessage.key;
            this.value = failedMessage.value;
            this.message = failedMessage.message;
            this.failureReason = failedMessage.failureReason;
            this.createTime = failedMessage.createTime;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder partition(Integer partition) {
            this.partition = partition;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder failureReason(String failureReason) {
            this.failureReason = failureReason;
            return this;
        }

        public Builder createTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public FailedMessage build() {
            return new FailedMessage(this);
        }
    }
}
