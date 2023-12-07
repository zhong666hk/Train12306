package com.wbu.train.batch.controller;

import com.wbu.train.batch.feign.BusinessFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private BusinessFeign businessFeign;
    @GetMapping("/hello")
    public String hello(){
        return businessFeign.hello();
    }
}
