package com.xiangkai.community.event;

import com.xiangkai.community.model.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class EventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void fireEvent(Event event) {
        kafkaTemplate.send(event.getTopic(), event.toJson()).addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("消息发送失败, 失败原因:" + ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                LOGGER.info(String.format("消息发送成功:\n" +
                                "producerRecord:[%s], recordMetadata:[%s]",
                        result.getProducerRecord(), result.getRecordMetadata()));
            }
        });
    }

}
