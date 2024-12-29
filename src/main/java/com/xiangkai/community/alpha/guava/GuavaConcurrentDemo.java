package com.xiangkai.community.alpha.guava;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GuavaConcurrentDemo {

    private static final Logger log = LoggerFactory.getLogger(GuavaConcurrentDemo.class);

    public static void main(String[] args) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("error-handling-thread-%d")
                .setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread thread, Throwable throwable) {
                        log.info("线程 {} 出现异常: {}", thread.getName(), throwable.getMessage());
                    }
                })
                .build();

        threadFactory.newThread(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("这是一个异常");
            }
        }).start();

        CyclicBarrier barrier = new CyclicBarrier(2);

        for (int i = 0; i < 2; i++) {
            final int threadNum = i;
            new Thread(() -> {
                try {
                    System.out.println("线程 " + threadNum + " 正在执行任务");
                    // 模拟线程执行任务的时间
                    Thread.sleep(1000);
                    System.out.println("线程 " + threadNum + " 到达屏障点");
                    barrier.await();
                    System.out.println("线程 " + threadNum + " 继续执行后续任务");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();

            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(100), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());


            Vector<String> vector = new Vector<>();

            List<String> list = new ArrayList<>();

            List<String> strings = Collections.synchronizedList(list);


        }
        int[] intArray = {1, 2, 3};

        List<Integer> integers = IntStream.of(intArray).boxed().collect(Collectors.toList());

        for (Integer integer : integers) {
            System.out.println(integer);
        }
    }

}
