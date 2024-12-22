package com.xiangkai.community.alpha.bean.factorybean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Objects;

@RestController
public class AlphaUserController implements InitializingBean, ApplicationContextAware {

    // 在类加载的初始化阶段就已经完成赋值
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AlphaUserService userService;

    public AlphaUserController(AlphaUserService userService) {
        this.userService = userService;
        logger.info("AlphaUserController constructor#getUserName: {}", userService.getUserName());
    }

    public String getUserName() {
        return userService.getUserName();
    }

    @PostConstruct
    public void init() {
        logger.info("AlphaUserController#PostConstruct#getUserName: {}", userService.getUserName());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("AlphaUserController#afterPropertiesSet");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("AlphaUserController#setApplicationContext");

        // 获取AlphaUserService Bean
        AlphaUserService userService0 = applicationContext.getBean(AlphaUserService.class);
        logger.info("AlphaUserController#setApplicationContext#userService0.getUserName: {}", userService0.getUserName());

        // 获取AlphaUserServiceFactoryBean Bean
        AlphaUserServiceFactoryBean alphaUserServiceFactoryBean = applicationContext.getBean(AlphaUserServiceFactoryBean.class);
        try {
            logger.info("AlphaUserController#setApplicationContext#alphaUserServiceFactoryBean.getObject()).getUserName: {}", Objects.requireNonNull(alphaUserServiceFactoryBean.getObject()).getUserName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 获取AlphaUserController Bean
        AlphaUserController bean = applicationContext.getBean(AlphaUserController.class);
        String userName = bean.getUserName();
        logger.info("AlphaUserController#setApplicationContext#alphaUserController#getUserName: {}", userName);
    }
}
