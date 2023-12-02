package com.wbu.train.batch.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 1.创建一个类实现Job接口
 */
@DisallowConcurrentExecution
public class QuartZJobTest implements Job {
    //    定时任务执行的方法
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("定时任务执行，当前时间：" + System.currentTimeMillis());

    }
}
