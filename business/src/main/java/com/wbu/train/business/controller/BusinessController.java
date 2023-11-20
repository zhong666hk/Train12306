package com.wbu.train.business.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business")
public class BusinessController {
    @GetMapping("/hello")
    public String hello(){
        return "hello business!!!";
    }
}
