package com.xiangkai.community.alpha.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Phaser;

public class PhaserDemo {
    private static final Logger log = LoggerFactory.getLogger(PhaserDemo.class);

    public static void main(String[] args) {

        int threadNums = 5;
        Phaser phaser = new Phaser(threadNums);

        for (int i = 0; i < threadNums; i++) {
            final int threadId = i;

            new Thread(() -> {
                log.info("线程-{}开始第一阶段", threadId);
                phaser.arriveAndAwaitAdvance();
                log.info("线程-{}开始第二阶段", threadId);
                phaser.arriveAndAwaitAdvance();
                log.info("线程-{}开始第三阶段", threadId);
                phaser.arriveAndAwaitAdvance();
            }).start();
        }
    }
}
