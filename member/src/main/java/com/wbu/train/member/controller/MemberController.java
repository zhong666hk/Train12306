package com.wbu.train.member.controller;

import cn.hutool.core.util.ObjectUtil;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.LoginResp;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.member.req.MemberLoginReq;
import com.wbu.train.member.req.MemberRegisterReq;
import com.wbu.train.member.req.MemberSendCodeReq;
import com.wbu.train.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @LogAnnotation
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/reject")
    public String reject() {
        return "reject";
    }

    @LogAnnotation
    @PostMapping("/register")
    public CommonRespond<Long> register(@Valid MemberRegisterReq memberRegisterReq) {
        if (ObjectUtil.isEmpty(memberRegisterReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        return memberService.register(memberRegisterReq);
    }

    /**
     * 获取短信验证码
     * @param memberSendCodeReq
     * @return
     */
    @LogAnnotation
    @PostMapping("/sendCode")
    public CommonRespond<String> sendCode(@Valid @RequestBody MemberSendCodeReq memberSendCodeReq) {
        if (ObjectUtil.isEmpty(memberSendCodeReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        return memberService.sendCode(memberSendCodeReq);
    }

    /**
     * 登录接口
     * @param memberLoginReq
     * @return
     */
    @LogAnnotation
    @PostMapping("/login")
    public CommonRespond<LoginResp> login(@Valid @RequestBody MemberLoginReq memberLoginReq) {
        if (ObjectUtil.isEmpty(memberLoginReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        return memberService.login(memberLoginReq);
    }

}
