package com.wbu.train.batch.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

/**
 * 1.创建一个类实现Job接口
 */
@DisallowConcurrentExecution
public class QuartZJobTest2 implements Job {
    //    定时任务执行的方法
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        StringJoiner joiner = new StringJoiner("\t");
        joiner.add("定时任务的触发2");
        joiner.add(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        System.out.println(joiner);
    }
}
