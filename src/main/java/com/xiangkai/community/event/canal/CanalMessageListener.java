package com.xiangkai.community.event.canal;

import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Component
public class CanalMessageListener implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(CanalMessageListener.class);

    private final static Integer BATCH_SIZE = 1000;

    private final static String SUBSCRIBE = "nowcoder.discuss_post";

    private final CanalMessageHandler messageHandler;

    @Autowired
    public CanalMessageListener(CanalMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run(String... args) throws Exception {
        startListening();
    }

    public void startListening() {
        // 创建链接
        InetSocketAddress socketAddress = new InetSocketAddress(AddressUtils.getHostIp(), 11111);

        CanalConnector connector = CanalConnectors.newSingleConnector(socketAddress, "example", "", "");

        try {
            connector.connect();
            connector.subscribe(SUBSCRIBE);
            connector.rollback();
            LOGGER.info("Initial canal listening complete!");
            while (true) {
                Message message = connector.getWithoutAck(BATCH_SIZE); // 获取指定数量的数据
                messageHandler.handle(message);
                connector.ack(message.getId());
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            LOGGER.error("Error create listening to canal messages: {}", e.getMessage(), e);
        } finally {
            connector.disconnect();
        }
    }
}
