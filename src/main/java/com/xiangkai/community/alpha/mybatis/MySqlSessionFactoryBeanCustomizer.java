package com.xiangkai.community.alpha.mybatis;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MySqlSessionFactoryBeanCustomizer implements SqlSessionFactoryBeanCustomizer {
    @Override
    public void customize(SqlSessionFactoryBean factoryBean) {
        System.out.println("自定义SqlSessionFactoryBeanCustomizer");
    }
}
