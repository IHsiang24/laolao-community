package com.xiangkai.community.entity;

import lombok.Data;

public class Page {

    // 当前页
    private Integer current = 1;

    // 每页上限
    private Integer limit = 10;

    // 数据总数(用于计算总页数)
    private Integer rows;

    // 查询路径(用于复用分页链接)
    private String path;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        if (rows > 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     */
    public Integer getOffset() {
        // current * limit - limit
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     */
    public Integer getTotal() {
        // rows / limit
        if (rows % limit == 0) {
            return rows / limit;
        }
        return (rows / limit) + 1;
    }

    /**
     * 获取起始页
     */
    public Integer getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }

    /**
     * 获取结束页
     */
    public Integer getTo() {
        int to = current + 2;
        int total = getTotal();
        return Math.min(to, total);
    }
}
