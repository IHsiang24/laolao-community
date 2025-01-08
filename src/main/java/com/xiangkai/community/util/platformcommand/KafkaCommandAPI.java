package com.xiangkai.community.util.platformcommand;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Kafka命令行执行工具
 */
public class KafkaCommandAPI {

    private static final Logger log = LoggerFactory.getLogger(KafkaCommandAPI.class);

    public static void main(String[] args) {
        listPartitionNums();
    }

    /**
     * 获取各个主题对应的分区数量
     */
    public static void listPartitionNums() {
        String bootstrapServers = "140.246.24.208:9092";
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        try (AdminClient adminClient = AdminClient.create(props)) {
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(true);
            ListTopicsResult topicsResult = adminClient.listTopics(options);
            KafkaFuture<Set<String>> names = topicsResult.names();

            DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(names.get());
            Map<String, TopicDescription> descriptionMap = describeTopicsResult.all().get();
            descriptionMap.forEach((name, description) -> {
                log.info("Topic: {}, 分区数量: {}", name, description.partitions().size());
            });
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 创建主题，并指定分区数量
     */
    public static void createTopicWithPartitionNums() {
        String bootstrapServers = "140.246.24.208:9092";
        String topicName = "PARTITION-NUM-TEST";
        int partitionCount = 3;
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        try (AdminClient adminClient = AdminClient.create(props)) {
            NewTopic newTopic = new NewTopic(topicName, partitionCount, (short) 1);
            adminClient.createTopics(Collections.singleton(newTopic));
            log.info("主题 {} 创建成功，分区数量为 {}", topicName, partitionCount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
