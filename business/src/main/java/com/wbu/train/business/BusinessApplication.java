package com.wbu.train.business;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.wbu.train") //扫描公共模块
@MapperScan("com.wbu.train.business.mapper")
//@MapperScan("com.wbu.train.*.mapper") 和视频的区别
public class BusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class,args);
    }
}
