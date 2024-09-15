package com.xiangkai.community.event;

import com.xiangkai.community.model.entity.Event;

public abstract class AbstractEventHandler implements EventHandler {

    public AbstractEventHandler() {
        EventHandlerFactory.registry(getType(), this);
    }

    public abstract void handle(Event event);

    public abstract String getType();

}
