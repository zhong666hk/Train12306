package com.wbu.train.business.train_carriage.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.business.train_carriage.domain.TrainCarriage;
import com.wbu.train.business.train_carriage.req.TrainCarriageQueryReq;
import com.wbu.train.business.train_carriage.req.TrainCarriageSaveReq;
import com.wbu.train.business.train_carriage.resp.TrainCarriageQueryResp;
import com.wbu.train.business.train_carriage.service.TrainCarriageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/train_carriage")
public class TrainCarriageController {
    public static final Logger LOG = LoggerFactory.getLogger(TrainCarriageController.class);

    @Autowired
    private TrainCarriageService trainCarriageService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody TrainCarriageSaveReq trainCarriageSaveReq) {
        if (ObjectUtil.isEmpty(trainCarriageSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try{
            if (trainCarriageService.saveTrainCarriage(trainCarriageSaveReq)) {
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
    public CommonRespond<Page<TrainCarriageQueryResp>> query_list(@Valid TrainCarriageQueryReq trainCarriageQueryReq) {

        Page<TrainCarriageQueryResp> page = trainCarriageService.queryTrainCarriages(trainCarriageQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (trainCarriageService.deleteById(id)){
            return CommonRespond.succeed("删除成功",true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }
}
