package com.xiangkai.community.alpha.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

public class BeanInitMethodOrderDemo implements InitializingBean, BeanPostProcessor,
        BeanFactoryAware, DisposableBean {

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanInitMethodOrderDemo.setBeanFactory");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("BeanInitMethodOrderDemo.afterPropertiesSet");
    }

    public BeanInitMethodOrderDemo() {
        System.out.println("BeanInitMethodOrderDemo constructor");
    }

    @PostConstruct
    public void init() {
        System.out.println("BeanInitMethodOrderDemo postConstruct");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanInitMethodOrderDemo postProcessBeforeInitialization");
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanInitMethodOrderDemo postProcessAfterInitialization");
        StringBuilder sb = new StringBuilder();
        sb.append("sss").append(1);
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
