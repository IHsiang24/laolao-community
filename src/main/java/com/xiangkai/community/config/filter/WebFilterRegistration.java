package com.xiangkai.community.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class WebFilterRegistration {

    private static final int INDEX_WEB_FILTER_ORDER = 0;
    private static final String INDEX_WEB_FILTER_NAME = "indexWebFilter";

    public FilterRegistrationBean<IndexWebFilter> IndexWebFilterRegistration() {

        FilterRegistrationBean<IndexWebFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new IndexWebFilter());
        registrationBean.setOrder(INDEX_WEB_FILTER_ORDER);
        registrationBean.setName(INDEX_WEB_FILTER_NAME);
        registrationBean.setEnabled(true);

        return registrationBean;
    }
}
