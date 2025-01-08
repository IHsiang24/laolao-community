package com.xiangkai.community.alpha.bean;

import org.springframework.stereotype.Service;

public class BeanB {

    private BeanA beanA;

    public BeanB(BeanA beanA) {
        this.beanA = beanA;
        this.beanA.m1();
    }
}
