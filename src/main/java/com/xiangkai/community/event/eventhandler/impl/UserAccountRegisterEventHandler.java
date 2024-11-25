package com.xiangkai.community.event.eventhandler.impl;

import com.xiangkai.community.event.eventhandler.EventHandler;
import com.xiangkai.community.model.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class UserAccountRegisterEventHandler implements EventHandler {

    @Override
    public void handle(Event event) {
        // todo 生成验证码，发送注册激活邮件等，放入阻塞队列，开一个线程池来消费
    }
}
