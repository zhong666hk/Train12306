package com.wbu.train.member.daily_train_ticket.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.business.daily_train_ticket.req.DailyTrainTicketQueryReq;
import com.wbu.train.business.daily_train_ticket.resp.DailyTrainTicketQueryResp;
import com.wbu.train.business.daily_train_ticket.service.DailyTrainTicketService;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.respon.CommonRespond;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/member/daily_train_ticket")
public class TrainTicketController {
    public static final Logger LOG = LoggerFactory.getLogger(TrainTicketController.class);

    @Autowired
    private DailyTrainTicketService dailyTrainTicketService;

    @LogAnnotation
    @GetMapping("/query_list")
    public CommonRespond<Page<DailyTrainTicketQueryResp>> query_list(@Valid DailyTrainTicketQueryReq dailyTrainTicketQueryReq) {

        Page<DailyTrainTicketQueryResp> page = dailyTrainTicketService.queryDailyTrainTickets(dailyTrainTicketQueryReq);
        return CommonRespond.succeed(page);
    }
}
