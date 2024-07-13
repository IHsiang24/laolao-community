package com.xiangkai.community.model.entity;

import java.util.Date;
import java.util.Objects;

public class LoginTicket {

    private Integer id;

    private Integer userId;

    private String ticket;

    private Integer status;

    private Date firstLoginTime;

    private Date expired;

    public Integer getId() {
        return id;
    }

    public LoginTicket setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public LoginTicket setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getTicket() {
        return ticket;
    }

    public LoginTicket setTicket(String ticket) {
        this.ticket = ticket;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public LoginTicket setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getFirstLoginTime() {
        return firstLoginTime;
    }

    public LoginTicket setFirstLoginTime(Date firstLoginTime) {
        this.firstLoginTime = firstLoginTime;
        return this;
    }

    public Date getExpired() {
        return expired;
    }

    public LoginTicket setExpired(Date expired) {
        this.expired = expired;
        return this;
    }

    public LoginTicket build() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginTicket that = (LoginTicket) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(ticket, that.ticket) &&
                Objects.equals(status, that.status) &&
                Objects.equals(expired, that.expired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, ticket, status, expired);
    }

    @Override
    public String toString() {
        return "LoginTicket{" +
                "id=" + id +
                ", userId=" + userId +
                ", ticket='" + ticket + '\'' +
                ", status=" + status +
                ", firstLoginTime=" + firstLoginTime +
                ", expired=" + expired +
                '}';
    }

}
