package com.xiangkai.community.alpha.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Exchanger;

@SuppressWarnings("all")
public class ExchangerDemo {
    private static final Logger log = LoggerFactory.getLogger(ExchangerDemo.class);

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            try {
                String data1 = "线程1的数据";
                log.info("线程-{}准备交换数据", Thread.currentThread().getName());
                String exchangeData1 = exchanger.exchange(data1);
                log.info("线程-{}交换后的数据数据: {}", Thread.currentThread().getName(), exchangeData1);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

        }, "线程1").start();

        new Thread(() -> {
            try {
                String data2 = "线程2的数据";
                log.info("线程-{}准备交换数据", Thread.currentThread().getName());
                String exchangeData2 = exchanger.exchange(data2);
                log.info("线程-{}交换后的数据数据: {}", Thread.currentThread().getName(), exchangeData2);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

        }, "线程2").start();
    }
}
