package com.wbu.train.getway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class GatewayController {
    @RequestMapping("/hello")
    public String hello(){
        return "helloGetway";
    }
}
