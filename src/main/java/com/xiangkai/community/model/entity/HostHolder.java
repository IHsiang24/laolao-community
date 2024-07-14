package com.xiangkai.community.model.entity;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {

    private final ThreadLocal<User> local = new ThreadLocal<>();

    public void set(User value) {
        local.set(value);
    }

    public User get() {
        return local.get();
    }

    public void destroy() {
        local.remove();
    }
}
