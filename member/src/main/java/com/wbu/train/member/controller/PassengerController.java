package com.wbu.train.member.controller;

import cn.hutool.core.util.ObjectUtil;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.member.req.PassengerSaveReq;
import com.wbu.train.member.service.PassengerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    public static final Logger LOG = LoggerFactory.getLogger(PassengerController.class);

    @Autowired
    private PassengerService passengerService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody PassengerSaveReq passengerSaveReq) {
        if (ObjectUtil.isEmpty(passengerSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try{
            if (passengerService.savePassenger(passengerSaveReq)) {
                return CommonRespond.succeed("添加乘客成功！！！",true);
            }
        }catch (Exception e){
            LOG.error(e.getMessage());
            return CommonRespond.error(AppExceptionExample.PASSENGER_SAVE_ERROR);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_SAVE_ERROR);
    }
}