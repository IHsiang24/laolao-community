package com.xiangkai.community.task.quartz;

@Deprecated
public abstract class AbstractQuartzTask {

    public final static String DEFAULT_TARGETMETHOD = "execute";

    public abstract void execute() throws Exception;

}
