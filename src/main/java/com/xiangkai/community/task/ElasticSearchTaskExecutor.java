package com.xiangkai.community.task;

import com.xiangkai.community.annotation.TaskExecutor;

@TaskExecutor(taskName = "ElasticSearchTaskExecutor")
public class ElasticSearchTaskExecutor implements CronJobExecutor {
    @Override
    public String cron() {
        return null;
    }

    @Override
    public void execute() {

    }
}
