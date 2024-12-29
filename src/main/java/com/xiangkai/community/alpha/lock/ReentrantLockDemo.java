package com.xiangkai.community.alpha.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    private static final Logger log = LoggerFactory.getLogger(ReentrantLockDemo.class);

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        lock.lock();
        try {
            log.info("lock acquired");
        } catch (Exception e) {
            log.error("lock acquired exception", e);
        } finally {
            lock.unlock();
        }
    }
}
