package com.xiangkai.community.event.canal;

import com.alibaba.otter.canal.protocol.Message;

public interface MessageHandler {
    void handle(Message message);
}
