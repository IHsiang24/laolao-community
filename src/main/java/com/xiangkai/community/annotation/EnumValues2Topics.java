package com.xiangkai.community.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 动态注入KafkaListener的topics值
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValues2Topics {

    /**
     * 指定要注入的Enum类型
     */
    Class<? extends Enum<?>> value();

    /**
     * 指定要使用Enum的什么方法注入
     */
    String method();
}
