package com.xiangkai.community.model.entity;

import java.util.Date;

public class DiscussPost {
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private Integer type;
    private Integer status;
    private Date createTime;
    private Integer commentCount;
    private Double score;

    public Integer getId() {
        return id;
    }

    public DiscussPost setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public DiscussPost setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DiscussPost setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DiscussPost setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public DiscussPost setType(Integer type) {
        this.type = type;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public DiscussPost setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public DiscussPost setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public DiscussPost setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public Double getScore() {
        return score;
    }

    public DiscussPost setScore(Double score) {
        this.score = score;
        return this;
    }

    public DiscussPost build() {
        return this;
    }
}
