package com.xiangkai.community.config;

import com.xiangkai.community.controller.interceptor.LoginInterceptor;
import com.xiangkai.community.controller.interceptor.LoginRequiredInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        "/**/*.css", "/**/*.js","/**/*.jpg",
                        "/**/*.png", "/**/*.doc", "/**/*.jpeg"
                );

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns(
                        "/**/*.css", "/**/*.js","/**/*.jpg",
                        "/**/*.png", "/**/*.doc", "/**/*.jpeg"
                );
    }
}
