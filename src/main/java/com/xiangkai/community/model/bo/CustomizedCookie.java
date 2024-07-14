package com.xiangkai.community.model.bo;

import javax.servlet.http.Cookie;

public class CustomizedCookie {

    // 原始cookie对象
    Cookie cookie;

    // cookie名
    String name;

    // cookie值
    String value;

    public Cookie getCookie() {
        return cookie;
    }

    public CustomizedCookie setCookie(Cookie cookie) {
        this.cookie = cookie;
        return this;
    }

    public String getName() {
        return name;
    }

    public CustomizedCookie setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CustomizedCookie setValue(String value) {
        this.value = value;
        return this;
    }

    public CustomizedCookie build() {
        return this;
    }
}
