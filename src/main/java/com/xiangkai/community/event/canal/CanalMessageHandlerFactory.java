package com.xiangkai.community.event.canal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CanalMessageHandlerFactory {

    private static final Logger log = LoggerFactory.getLogger(CanalMessageHandlerFactory.class);

    private static final Map<String, CanalMessageHandler> canalMessageHandlerMap =
            new HashMap<String, CanalMessageHandler>();

    public static void registry(String type, CanalMessageHandler canalMessageHandler) {
        canalMessageHandlerMap.put(type, canalMessageHandler);
    }

    public static CanalMessageHandler getCanalMessageHandler(String type) {
        return canalMessageHandlerMap.get(type);
    }
}
