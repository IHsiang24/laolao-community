package com.xiangkai.community.alpha.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    private static final Logger log = LoggerFactory.getLogger(CyclicBarrierDemo.class);

    public static void main(String[] args) {

        int threadNums = 5;

        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNums, () -> log.info("所有线程均到达屏障点！"));

        for (int i = 0; i < threadNums; i++) {
            final int threadId = i;

            new Thread(new Runnable() {
                public void run() {
                    try {
                        log.info("线程-{}开始执行", threadId);
                        Thread.sleep(1000);
                        log.info("线程-{}到达屏障点", threadId);
                        cyclicBarrier.await();
                        log.info("线程-{}继续执行", threadId);
                    } catch (InterruptedException | BrokenBarrierException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }).start();
        }
    }
}
