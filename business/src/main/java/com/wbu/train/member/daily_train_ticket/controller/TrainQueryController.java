package com.wbu.train.member.daily_train_ticket.controller;

import com.wbu.train.business.train.resp.TrainQueryResp;
import com.wbu.train.business.train.service.TrainService;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.respon.CommonRespond;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/business/member/train")
public class TrainQueryController {
    public final Logger LOG = LoggerFactory.getLogger(TrainQueryController.class);

    @Autowired
    private TrainService trainService;
    @LogAnnotation
    @GetMapping("/query_all")
    public CommonRespond<List<TrainQueryResp>> query_all() {
        List<TrainQueryResp> trainQueryRespList = trainService.queryAll();
        return CommonRespond.succeed(trainQueryRespList);
    }
}
