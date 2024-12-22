package com.xiangkai.community.alpha.bean.factorybean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlphaUserServiceConfig {
    @Bean
    public AlphaUserServiceFactoryBean userServiceFactoryBean() {
        return new AlphaUserServiceFactoryBean();
    }
}
