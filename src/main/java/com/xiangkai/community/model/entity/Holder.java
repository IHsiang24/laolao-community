package com.xiangkai.community.model.entity;

public class Holder<T> {

    private final ThreadLocal<T> local = new ThreadLocal<>();

    public void set(T value) {
        local.set(value);
    }

    public T get() {
        return local.get();
    }

    public void destroy() {
        local.remove();
    }
}
