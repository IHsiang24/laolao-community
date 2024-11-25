package com.xiangkai.community.alpha.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class AopDemoService{

    private static final Logger LOGGER = LoggerFactory.getLogger(AopDemoService.class);

    public void normalMethod() {
        LOGGER.info("AopDemoService normalMethod called");
    }

    public String testMethod() {
        LOGGER.info("AopDemoService testMethod called");
        return "AopDemoService testMethod called";
    }
}
