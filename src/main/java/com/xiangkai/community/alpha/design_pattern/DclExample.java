package com.xiangkai.community.alpha.design_pattern;

public class DclExample {

    private volatile static DclExample instance;

    private DclExample() {
    }

    public static DclExample getInstance() {
        if (instance == null) {
            synchronized (DclExample.class) {
                if (instance == null) {
                    instance = new DclExample();
                }
            }
        }
        return instance;
    }

}
