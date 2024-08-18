package com.xiangkai.community.config.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class WebFilterRegistration {

    @Autowired
    private IndexWebFilter indexWebFilter;

    @Bean
    public FilterRegistrationBean<IndexWebFilter> IndexWebFilterRegistration() {

        FilterRegistrationBean<IndexWebFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(indexWebFilter);
        registrationBean.setOrder(1);
        registrationBean.setName("indexWebFilter");
        registrationBean.setEnabled(true);

        return registrationBean;
    }
}
