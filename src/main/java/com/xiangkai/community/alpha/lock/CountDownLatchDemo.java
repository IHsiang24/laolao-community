package com.xiangkai.community.alpha.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    private static final Logger log = LoggerFactory.getLogger(CountDownLatchDemo.class);

    public static void main(String[] args) {
        int threadNums = 5;
        CountDownLatch latch = new CountDownLatch(threadNums);

        for (int i = 0; i < threadNums; i++) {

            final int threadId = i;

            new Thread(new Runnable() {
                public void run() {
                    try {
                        log.info("线程-{}开始执行", threadId);
                        Thread.sleep(1000);
                        log.info("线程-{}执行完毕", threadId);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        latch.countDown();
                    }
                }
            }).start();
        }

        try {
            log.info("线程-{}开始阻塞", Thread.currentThread().getName());
            latch.await();
            log.info("线程-{}被唤醒", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
