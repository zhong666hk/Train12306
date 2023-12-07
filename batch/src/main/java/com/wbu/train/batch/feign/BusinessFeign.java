package com.wbu.train.batch.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//@FeignClient("business") //注册中心的写法+负载均衡
@FeignClient(name="business",url = "http://127.0.0.1:8002/api/business/train") //单一指定的不需要注册中心和负载均衡
public interface BusinessFeign {
     @GetMapping(value = "/hello",headers = {"token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE3MDE5MjA5NDMsIm1vYmlsZSI6IjE2NjA3MjExNTAzIiwiaWQiOjE3Mjk0Mzk4MzI1NzQzOTg0NjQsImV4cCI6MTcwMjAwNzM0MywiaWF0IjoxNzAxOTIwOTQzfQ.-7V49L6kpCtyA_KVAmE7SdNVVapG8TiLFHXbhd8ANEM"})
    String hello();
}
