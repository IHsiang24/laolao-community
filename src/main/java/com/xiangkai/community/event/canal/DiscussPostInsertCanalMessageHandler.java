package com.xiangkai.community.event.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiangkai.community.constant.CommunityConstant;
import com.xiangkai.community.event.EventProducer;
import com.xiangkai.community.model.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscussPostInsertCanalMessageHandler extends AbstractCanalMessageHandler implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostInsertCanalMessageHandler.class);

    @Autowired
    private EventProducer producer;

    @Override
    public void handle(Message message) {

        long messageId = message.getId();
        List<CanalEntry.Entry> entries = message.getEntries();
        int size = entries.size();

        if (messageId == -1 || size == 0) {
            return;
        }

        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN ||
                    entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChange = null;

            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (InvalidProtocolBufferException e) {
                logger.error("解析RowChange错误：", e);
            }

            if (rowChange != null) {
                CanalEntry.EventType eventType = rowChange.getEventType();
                for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                    if (CanalEntry.EventType.INSERT == eventType) {
                        publish(rowData.getAfterColumnsList());
                    }
                }
            }

        }
    }

    @Override
    public String getType() {
        return "discussPostInsert";
    }

    private void publish(List<CanalEntry.Column> columns) {
        Map<String, Object> data = new HashMap<>();

        for (CanalEntry.Column column : columns) {
            data.put(column.getName(), column.getValue());
        }

        // 发布PUBLISH事件
        Event event = new Event.Builder()
                .eventTypeId(EVENT_TYPE_ID_PUBLISH)
                .topic(TOPIC_PUBLISH)
                .userId(Integer.valueOf((String)data.getOrDefault("user_id", null)))
                .entityType(ENTITY_TYPE_POST)
                .entityId(Integer.valueOf((String) data.getOrDefault("id", null)))
                .targetUserId(Integer.valueOf((String) data.getOrDefault("user_id", null)))
                .timestamp(System.currentTimeMillis())
                .data("discussPostId", Integer.valueOf((String) data.getOrDefault("user_id", null)))
                .build();

        producer.fireEvent(event);
    }
}
