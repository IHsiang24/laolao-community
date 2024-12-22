package com.xiangkai.community.alpha.bean.factorybean;

import org.springframework.beans.factory.FactoryBean;

public class AlphaUserServiceFactoryBean implements FactoryBean<AlphaUserService> {
    @Override
    public AlphaUserService getObject() throws Exception {
        return new AlphaUserServiceImpl();
    }

    @Override
    public Class<?> getObjectType() {
        return AlphaUserService.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
