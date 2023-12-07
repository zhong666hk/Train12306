package com.wbu.train.batch.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.wbu.train.batch.feign.BusinessFeign;
import com.wbu.train.common.respon.CommonRespond;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DailyTrainJob implements Job {
    @Autowired
    private BusinessFeign businessFeign;
    private Logger LOG = LoggerFactory.getLogger(DailyTrainJob.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        MDC.put("LOG_ID",System.currentTimeMillis()+ RandomUtil.randomString(3));
        LOG.info("生成15天后的车次数据开始");
        Date date = new Date();
        DateTime dateTime = DateUtil.offsetDay(date, 15);
        Date offsetDate = dateTime.toJdkDate();
        CommonRespond<Boolean> commonRespond = businessFeign.gen(offsetDate);
        LOG.info("生成15天后的车次数据完成结果{}",commonRespond);
    }
}
