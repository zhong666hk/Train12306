package com.wbu.train.business.train_seat.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.business.train_seat.domain.TrainSeat;
import com.wbu.train.business.train_seat.req.TrainSeatQueryReq;
import com.wbu.train.business.train_seat.req.TrainSeatSaveReq;
import com.wbu.train.business.train_seat.resp.TrainSeatQueryResp;
import com.wbu.train.business.train_seat.service.TrainSeatService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/train_seat")
public class TrainSeatController {
    public static final Logger LOG = LoggerFactory.getLogger(TrainSeatController.class);

    @Autowired
    private TrainSeatService trainSeatService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody TrainSeatSaveReq trainSeatSaveReq) {
        if (ObjectUtil.isEmpty(trainSeatSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try{
            if (trainSeatService.saveTrainSeat(trainSeatSaveReq)) {
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
    public CommonRespond<Page<TrainSeatQueryResp>> query_list(@Valid TrainSeatQueryReq trainSeatQueryReq) {

        Page<TrainSeatQueryResp> page = trainSeatService.queryTrainSeats(trainSeatQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (trainSeatService.deleteById(id)){
            return CommonRespond.succeed("删除成功",true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }
}
