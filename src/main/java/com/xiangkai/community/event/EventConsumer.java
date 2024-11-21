package com.xiangkai.community.event;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.xiangkai.community.annotation.EnumValues2Topics;
import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.model.entity.Event;
import com.xiangkai.community.event.renum.EventType;
import org.apache.commons.collections4.map.SingletonMap;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

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

    public void handle() {
        try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(Collections.singleton("LIKE"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    consumer.commitAsync(
                            new SingletonMap<>(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset())),
                            new OffsetCommitCallback() {
                                @Override
                                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                                    if (exception != null) {
                                        LOGGER.error("手动提交偏移量失败：", exception);
                                    } else {
                                        LOGGER.info("偏移量提交成功：topic：{}，partition：{}，offset：{}",
                                                record.topic(), record.partition(), record.offset());
                                    }
                                }
                            });
                }
            }
        } catch(Exception e){
            LOGGER.error("手动提交偏移量发生错误：", e);
        }
    }
}
