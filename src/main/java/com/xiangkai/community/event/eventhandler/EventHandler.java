package com.xiangkai.community.event.eventhandler;

import com.xiangkai.community.model.entity.Event;

public interface EventHandler {
    void handle(Event event);
}
