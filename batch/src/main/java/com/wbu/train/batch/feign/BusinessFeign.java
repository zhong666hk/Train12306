package com.wbu.train.batch.feign;

import com.wbu.train.common.respon.CommonRespond;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

//@FeignClient("business") //注册中心的写法+负载均衡
@FeignClient(name="business",url = "http://127.0.0.1:8002/api/business") //单一指定的不需要注册中心和负载均衡
public interface BusinessFeign {
     @GetMapping(value = "/train/hello",headers = {"token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE3MDE5MjA5NDMsIm1vYmlsZSI6IjE2NjA3MjExNTAzIiwiaWQiOjE3Mjk0Mzk4MzI1NzQzOTg0NjQsImV4cCI6MTcwMjAwNzM0MywiaWF0IjoxNzAxOTIwOTQzfQ.-7V49L6kpCtyA_KVAmE7SdNVVapG8TiLFHXbhd8ANEM"})
    String hello();

//    @GetMapping(value = "/daily_train/gen-daily/{date}",headers = {"token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE3MDE5MjA5NDMsIm1vYmlsZSI6IjE2NjA3MjExNTAzIiwiaWQiOjE3Mjk0Mzk4MzI1NzQzOTg0NjQsImV4cCI6MTcwMjAwNzM0MywiaWF0IjoxNzAxOTIwOTQzfQ.-7V49L6kpCtyA_KVAmE7SdNVVapG8TiLFHXbhd8ANEM"})
@GetMapping(value = "/daily_train/gen-daily/{date}")
CommonRespond<Boolean> gen(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}
