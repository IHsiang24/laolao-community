package com.xiangkai.community.event.canal;

public abstract class AbstractCanalMessageHandler implements CanalMessageHandler {

    public AbstractCanalMessageHandler() {
        CanalMessageHandlerFactory.registry(getType(), this);
    }
}
