package com.xiangkai.community.config;

import com.xiangkai.community.config.interceptor.DataInterceptor;
import com.xiangkai.community.config.interceptor.LoginInterceptor;
import com.xiangkai.community.config.interceptor.NoticeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private NoticeInterceptor noticeInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        "/**/*.css", "/**/*.js","/**/*.jpg",
                        "/**/*.png", "/**/*.doc", "/**/*.jpeg",
                        "/**/*.html"
                );

        registry.addInterceptor(noticeInterceptor)
                .excludePathPatterns(
                        "/**/*.css", "/**/*.js","/**/*.jpg",
                        "/**/*.png", "/**/*.doc", "/**/*.jpeg",
                        "/**/*.html"
                );

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns(
                        "/**/*.css", "/**/*.js","/**/*.jpg",
                        "/**/*.png", "/**/*.doc", "/**/*.jpeg",
                        "/**/*.html"
                );
    }
}
