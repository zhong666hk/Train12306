package com.wbu.train.batch.config;

import com.wbu.train.batch.job.MyJobFactory;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class SchedulerConfig {
    @Resource
    private MyJobFactory myJobFactory;

    /**
     *  覆盖了SpringBoot的方法所以要自己设置集群
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource")DataSource dataSource){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setJobFactory(myJobFactory);
        // 设置 Quartz 属性
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        // 启用集群
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setStartupDelay(5); // 延迟启动，以防止多个节点同时启动
        return schedulerFactoryBean;
    }

    private Properties quartzProperties() {
        Properties properties = new Properties();
        // 设置 Quartz 属性
        properties.setProperty("org.quartz.scheduler.instanceName", "MyClusteredScheduler");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");

        // 设置为集群模式
        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", "20000");

        return properties;
    }
}
