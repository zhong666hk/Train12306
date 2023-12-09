package com.wbu.train.business.daily_train_ticket.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.business.daily_train_ticket.domain.DailyTrainTicket;
import com.wbu.train.business.daily_train_ticket.req.DailyTrainTicketQueryReq;
import com.wbu.train.business.daily_train_ticket.req.DailyTrainTicketSaveReq;
import com.wbu.train.business.daily_train_ticket.resp.DailyTrainTicketQueryResp;
import com.wbu.train.business.daily_train_ticket.service.DailyTrainTicketService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/daily_train_ticket")
public class DailyTrainTicketController {
    public static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketController.class);

    @Autowired
    private DailyTrainTicketService dailyTrainTicketService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody DailyTrainTicketSaveReq dailyTrainTicketSaveReq) {
        if (ObjectUtil.isEmpty(dailyTrainTicketSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try{
            if (dailyTrainTicketService.saveDailyTrainTicket(dailyTrainTicketSaveReq)) {
                return CommonRespond.succeed("乘客添加或修改成功！！！",true);
            }
        }catch (Exception e){
            LOG.error(e.getMessage());
            return CommonRespond.error(AppExceptionExample.PASSENGER_SAVE_ERROR);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_SAVE_ERROR);
    }


    @LogAnnotation
    @GetMapping("/query_list")
    public CommonRespond<Page<DailyTrainTicketQueryResp>> query_list(@Valid DailyTrainTicketQueryReq dailyTrainTicketQueryReq) {

        Page<DailyTrainTicketQueryResp> page = dailyTrainTicketService.queryDailyTrainTickets(dailyTrainTicketQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (dailyTrainTicketService.deleteById(id)){
            return CommonRespond.succeed("删除成功",true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }
}
