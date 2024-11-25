package com.xiangkai.community.event.canal;

import com.alibaba.otter.canal.protocol.Message;

public interface CanalMessageHandler {

    void handle(Message message);

    String getType();
}
