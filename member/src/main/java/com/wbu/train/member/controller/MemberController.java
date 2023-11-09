package com.wbu.train.member.controller;

import cn.hutool.core.util.ObjectUtil;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.member.domain.Member;
import com.wbu.train.member.req.MemberRegisterReq;
import com.wbu.train.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @LogAnnotation
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping("/reject")
    public String reject(){
        return "reject";
    }

    @LogAnnotation
    @RequestMapping("/register")
    public CommonRespond<Boolean> register(MemberRegisterReq memberRegisterReq){
        if (ObjectUtil.isEmpty(memberRegisterReq)){
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        return memberService.register(memberRegisterReq);
    }

}
