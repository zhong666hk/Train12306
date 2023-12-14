package com.wbu.train.member.daily_train_ticket.controller;

import com.wbu.train.business.station.resp.StationQueryResp;
import com.wbu.train.business.station.service.StationService;
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
@RequestMapping("/business/member/station")
public class StationQueryController {
    public static final Logger LOG = LoggerFactory.getLogger(StationQueryController.class);

    @Autowired
    private StationService stationService;

    @LogAnnotation
    @GetMapping("/query_all")
    public CommonRespond<List<StationQueryResp>> query_all() {
        List<StationQueryResp> stationQueryRespList = stationService.queryAll();
        return CommonRespond.succeed(stationQueryRespList);
    }
}
