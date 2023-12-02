package com.wbu.train.batch.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
//@EnableScheduling
public class SpringBootTestJo {

    @Scheduled(cron = "0/5 * * * * *")
    private void print(){
        System.out.println("SpringBootTestJo");
    }
}
