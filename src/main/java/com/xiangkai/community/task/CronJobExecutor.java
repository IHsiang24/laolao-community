package com.xiangkai.community.task;

public interface CronJobExecutor {

    /**
     * cron表达式
     */
    String cron();

    /**
     * 任务执行
     */
    void execute();
}
