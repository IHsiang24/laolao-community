package com.xiangkai.community.alpha.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

public class AopDemoCommandLineRunner implements CommandLineRunner {

    @Autowired
    private AopDemoService aopDemoService;

    @Override
    public void run(String... args) throws Exception {
        aopDemoService.testMethod();
    }
}
