package com.xiangkai.community.model.entity;

import java.util.Date;

public class Message {

    private Integer id;

    private Integer fromId;

    private Integer toId;

    private String conversationId;

    private String content;

    /**
     * 0-未读;1-已读;2-删除
     */
    private Integer status;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public Message setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getFromId() {
        return fromId;
    }

    public Message setFromId(Integer fromId) {
        this.fromId = fromId;
        return this;
    }

    public Integer getToId() {
        return toId;
    }

    public Message setToId(Integer toId) {
        this.toId = toId;
        return this;
    }

    public String getConversationId() {
        return conversationId;
    }

    public Message setConversationId(String conversationId) {
        this.conversationId = conversationId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public Message setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Message setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Message build() {
        return this;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", conversationId='" + conversationId + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
