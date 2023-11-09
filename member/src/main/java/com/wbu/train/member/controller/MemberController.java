package com.wbu.train.member.controller;

import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {
    @LogAnnotation
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
