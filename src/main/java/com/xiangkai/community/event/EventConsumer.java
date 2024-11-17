package com.xiangkai.community.event;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.xiangkai.community.annotation.EnumValues2Topics;
import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.model.entity.Event;
import com.xiangkai.community.event.renum.EventType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = {TOPIC_COMMENT})
    @EnumValues2Topics(value = EventType.class, method = "getType")
    public void handle(ConsumerRecord<String, String> record) {
        if (record == null || record.value() == null) {
            LOGGER.error("解析消息失败！");
            return;
        }

        Event event = JSONObject.parseObject(record.value(), new TypeReference<Event>() {}.getType(),
                Feature.SupportNonPublicField);

        // 根据事件id获取事件类型
        String eventType = EventHandlerFactory.getEventType(event.getEventTypeId());
        AbstractEventHandler handler = EventHandlerFactory.getHandler(eventType);

        // 事件处理器处理对应类型的事件
        handler.handle(event);
    }
}
