package com.xiangkai.community.model.vo;

import com.xiangkai.community.model.entity.Message;
import com.xiangkai.community.model.entity.User;

public class NoticeVO {
    /**
     * 消息体本身
     */
    private Message message;

    /**
     * 点赞、关注、回复的用户
     */
    private User user;

    /**
     * 消息发送人
     */
    private User fromUser;

    /**
     * 点赞、关注、回复对应的实体
     */
    private Integer entityType;

    /**
     * 点赞、关注、回复对应的实体ID，包括userId
     */
    private Integer entityId;

    /**
     * 点赞、关注、回复对应的postId（如果有）
     */
    private Integer postId;

    /**
     * 消息总数
     */
    private Integer count;

    /**
     * 未读消息数
     */
    private Integer unread;

    public NoticeVO() {
    }

    public NoticeVO(Builder builder) {
        this.message = builder.message;
        this.user = builder.user;
        this.fromUser = builder.fromUser;
        this.entityType = builder.entityType;
        this.entityId = builder.entityId;
        this.postId = builder.postId;
        this.count = builder.count;
        this.unread = builder.unread;
    }

    public Message getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public User getFromUser() {
        return fromUser;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public Integer getPostId() {
        return postId;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getUnread() {
        return unread;
    }

    @Override
    public String toString() {
        return "MessageVO{" +
                "message=" + message +
                ", user=" + user +
                ", fromUser=" + fromUser +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", postId=" + postId +
                ", count=" + count +
                ", unread=" + unread +
                '}';
    }

    public static class Builder {

        private Message message;

        private User user;

        private User fromUser;

        private Integer entityType;

        private Integer entityId;

        private Integer postId;

        private Integer count;

        private Integer unread;

        public Builder() {
        }

        public Builder(NoticeVO vo) {
            this.message = vo.message;
            this.user = vo.user;
            this.fromUser = vo.fromUser;
            this.entityType = vo.entityType;
            this.entityId = vo.entityId;
            this.postId = vo.postId;
            this.count = vo.count;
            this.unread = vo.unread;
        }

        public Builder message(Message message) {
            this.message = message;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder fromUser(User fromUser) {
            this.fromUser = fromUser;
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

        public Builder postId(Integer postId) {
            this.postId = postId;
            return this;
        }

        public Builder count(Integer count) {
            this.count = count;
            return this;
        }

        public Builder unread(Integer unread) {
            this.unread = unread;
            return this;
        }

        public NoticeVO build() {
            return new NoticeVO(this);
        }
    }
}
