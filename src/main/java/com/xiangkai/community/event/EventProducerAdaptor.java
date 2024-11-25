package com.xiangkai.community.event;

import com.xiangkai.community.dao.mapper.FailedMessagesMapper;
import com.xiangkai.community.model.entity.FailedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Date;

@Component
public class EventProducerAdaptor {

    @Value("${spring.kafka.admin.properties.num.partitions}")
    private int numPartitions;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final FailedMessagesMapper failedMessagesMapper;

    private static final int MAX_RETRIES = 3;

    private static final long RETRY_INTERVAL_MS = 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProducerAdaptor.class);

    @Autowired
    public EventProducerAdaptor(KafkaTemplate<String, String> kafkaTemplate, FailedMessagesMapper failedMessagesMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.failedMessagesMapper = failedMessagesMapper;
    }

    public void sendMessageWithRetry(String topic, String message) {
        RetryTemplate retryTemplate = buildRetryTemplate();
        try {
            retryTemplate.execute(new RetryCallback<Void, Throwable>() {
                @Override
                public Void doWithRetry(RetryContext context) throws Throwable {
                    int hash = hash(message);
                    ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, hash % numPartitions, String.valueOf(hash), message);
                    future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                        @Override
                        public void onSuccess(SendResult<String, String> result) {
                            LOGGER.info("消息发送成功，主题: {}, 分区: {}, 偏移量: {}",
                                    result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }

                        @Override
                        public void onFailure(Throwable ex) {
                            LOGGER.error("消息发送失败: ", ex);

                            failedMessagePersistence(topic, message, ex);
                        }
                    });
                    return null;
                }
            });
        } catch (Throwable e) {
            LOGGER.error("消息重新发送失败，重试次数已达最大值", e);
            failedMessagePersistence(topic, message, e);
        }
    }

    private RetryTemplate buildRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_RETRIES + 1);
        retryTemplate.setRetryPolicy(retryPolicy);
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(RETRY_INTERVAL_MS);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    private int hash(Object key) {
        int h;
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        return hash > 0 ? hash : -hash;
    }

    private Integer failedMessagePersistence(String topic, String message, Throwable ex) {
        FailedMessage failedMessage = new FailedMessage.Builder()
                .key(null)
                .value(null)
                .topic(topic)
                .partition(null)
                .message(message)
                .failureReason(ex.getMessage())
                .createTime(new Date())
                .build();
        return failedMessagesMapper.insertFailedMessage(failedMessage);
    }
}