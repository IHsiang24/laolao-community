package com.xiangkai.community.event.canal;

import com.alibaba.otter.canal.protocol.Message;
import org.springframework.stereotype.Component;

@Component
public class CanalMessageHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        // todo 实现消息解析功能
    }
}
