package com.xiangkai.community.alpha.concurrent;

import com.xiangkai.community.util.LocalCommandExecutor;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class ConcurrentExample {

    public static void main(String[] args) {
        final ThreadPoolExecutor threadPool =
                new ThreadPoolExecutor(
                        3,
                        5,
                        300,
                        TimeUnit.SECONDS,
                        new SynchronousQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.AbortPolicy());

        Future<String> submit = threadPool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("提交执行任务，执行线程：" + Thread.currentThread().getName());
            }
        }, "执行成功！");

        try {
            System.out.println(submit.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }


        Future<Integer> call = threadPool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("提交执行任务，执行线程：" + Thread.currentThread().getName());
                return 43;
            }
        });

        try {
            System.out.println(call.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        CompletableFuture<LocalCommandExecutor.CommandExecuteResult> ipconfig = CompletableFuture.supplyAsync(new Supplier<LocalCommandExecutor.CommandExecuteResult>() {
            @Override
            public LocalCommandExecutor.CommandExecuteResult get() {
                return LocalCommandExecutor.execute("ipconfig", new LocalCommandExecutor.CommandCallback<LocalCommandExecutor.CommandExecuteResult>() {
                    @Override
                    public LocalCommandExecutor.CommandExecuteResult doWithProcessor(LocalCommandExecutor.Processor processor) throws Exception {
                        return processor.result();
                    }
                });
            }
        });

        try {
            System.out.println(ipconfig.get().getInput());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
