package com.xiangkai.community.event;

import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.event.renum.EventType;

import java.util.HashMap;
import java.util.Map;

public class EventHandlerFactory implements CommunityConstant {

    private static final Map<EventType, AbstractEventHandler> handlerMap = new HashMap<>();

    private static final Map<Integer, String> eventTypeMap = new HashMap<>();

    static {

        // 事件ID初始化为评论事件ID
        Integer eventTypeId = EVENT_TYPE_ID_COMMENT;

        for (EventType value : EventType.values()) {
            eventTypeMap.put(eventTypeId, value.toString());
            eventTypeId++;
        }
    }

    public static void registry(String eventType, AbstractEventHandler handler) {
        handlerMap.put(EventType.valueOf(eventType), handler);
    }

    public static AbstractEventHandler getHandler(String eventType) {
        return handlerMap.get(EventType.valueOf(eventType));
    }

    public static String getEventType(Integer eventTypeId) {
        return eventTypeMap.get(eventTypeId);
    }
}
