package com.wbu.train.batch.controller;

import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {
    @LogAnnotation
    @GetMapping("/hello")
    public String hello (){
        return "hello batch!!!";
    }
}
