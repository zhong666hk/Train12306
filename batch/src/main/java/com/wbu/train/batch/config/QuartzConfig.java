package com.wbu.train.batch.config;

import com.wbu.train.batch.job.QuartZJobTest;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class QuartzConfig {
    /**
     * 2. 声明定时任务
     *
     * @return
     */
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(QuartZJobTest.class)
                // 1.命名 2.分组
                .withIdentity("TestJob", "test")
                // 持久存储
                .storeDurably()
                .build();
    }

    /**
     * 3.声明触发器，生么时候触发任务
     */
    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                // 触发器触发的任务forJob(name) name=TestJob
                .forJob(jobDetail)
                .withIdentity("trigger1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?"))
                .startNow()
                .build();
    }
}
