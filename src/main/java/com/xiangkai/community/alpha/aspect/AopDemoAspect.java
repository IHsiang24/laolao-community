package com.xiangkai.community.alpha.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class AopDemoAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopDemoAspect.class);

    @Pointcut(value = "execution(* com.xiangkai.community.alpha.aspect.AopDemoService.test*(..)))")
    public void pointCut() {}

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) {
        LOGGER.info("Before method invoked: {}", joinPoint.getSignature().getName());
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.info("Around before method invoked: {}", joinPoint.getSignature().getName());
        joinPoint.proceed();
        LOGGER.info("Around after method invoked: {}", joinPoint.getSignature().getName());
        return null;
    }

    @AfterReturning(value = "pointCut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        LOGGER.info("AfterReturning method invoked: {}, returnValue: {}",
                joinPoint.getSignature().getName(), returnValue);
    }

    @After(value = "pointCut()")
    public void after(JoinPoint joinPoint) {
        LOGGER.info("After method invoked: {}", joinPoint.getSignature().getName());
    }
    @AfterThrowing(value = "pointCut()")
    public void afterThrowing(JoinPoint joinPoint) {
        LOGGER.info("AfterThrowing method invoked: {}", joinPoint.getSignature().getName());
    }
}
