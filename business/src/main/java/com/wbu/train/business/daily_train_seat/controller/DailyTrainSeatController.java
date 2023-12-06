package com.wbu.train.business.daily_train_seat.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.business.daily_train_seat.domain.DailyTrainSeat;
import com.wbu.train.business.daily_train_seat.req.DailyTrainSeatQueryReq;
import com.wbu.train.business.daily_train_seat.req.DailyTrainSeatSaveReq;
import com.wbu.train.business.daily_train_seat.resp.DailyTrainSeatQueryResp;
import com.wbu.train.business.daily_train_seat.service.DailyTrainSeatService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/daily_train_seat")
public class DailyTrainSeatController {
    public static final Logger LOG = LoggerFactory.getLogger(DailyTrainSeatController.class);

    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody DailyTrainSeatSaveReq dailyTrainSeatSaveReq) {
        if (ObjectUtil.isEmpty(dailyTrainSeatSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try{
            if (dailyTrainSeatService.saveDailyTrainSeat(dailyTrainSeatSaveReq)) {
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
    public CommonRespond<Page<DailyTrainSeatQueryResp>> query_list(@Valid DailyTrainSeatQueryReq dailyTrainSeatQueryReq) {

        Page<DailyTrainSeatQueryResp> page = dailyTrainSeatService.queryDailyTrainSeats(dailyTrainSeatQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (dailyTrainSeatService.deleteById(id)){
            return CommonRespond.succeed("删除成功",true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }
}
