package com.xiangkai.community.annotation;

public @interface TaskExecutor {
    /**
     * 任务名
     */
    String taskName() default "";
}
