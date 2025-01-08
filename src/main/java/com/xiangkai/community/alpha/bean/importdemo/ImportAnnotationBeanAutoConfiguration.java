package com.xiangkai.community.alpha.bean.importdemo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAnnotation
@ConditionalOnMissingBean(value = ImportAnnotationBean.class)
public class ImportAnnotationBeanAutoConfiguration {
    public void m1() {
        System.out.println("ImportAnnotationBeanAutoConfiguration#m1");
    }
}
