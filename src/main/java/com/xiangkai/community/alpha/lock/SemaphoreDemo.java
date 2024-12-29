package com.xiangkai.community.alpha.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    private static final Logger log = LoggerFactory.getLogger(SemaphoreDemo.class);

    public static void main(String[] args) {
        int threadNums = 10;
        int permits = 3;

        Semaphore semaphore = new Semaphore(permits);

        for (int i = 0; i < threadNums; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    log.info("线程-{}获取许可证", threadId);
                    Thread.sleep(2000);
                    semaphore.release();
                    log.info("线程-{}释放许可证", threadId);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }).start();
        }
    }
}
