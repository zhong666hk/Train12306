package com.wbu.train.business.daily_train_carriage.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.business.daily_train_carriage.domain.DailyTrainCarriage;
import com.wbu.train.business.daily_train_carriage.req.DailyTrainCarriageQueryReq;
import com.wbu.train.business.daily_train_carriage.req.DailyTrainCarriageSaveReq;
import com.wbu.train.business.daily_train_carriage.resp.DailyTrainCarriageQueryResp;
import com.wbu.train.business.daily_train_carriage.service.DailyTrainCarriageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/daily_train_carriage")
public class DailyTrainCarriageController {
    public static final Logger LOG = LoggerFactory.getLogger(DailyTrainCarriageController.class);

    @Autowired
    private DailyTrainCarriageService dailyTrainCarriageService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody DailyTrainCarriageSaveReq dailyTrainCarriageSaveReq) {
        if (ObjectUtil.isEmpty(dailyTrainCarriageSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try{
            if (dailyTrainCarriageService.saveDailyTrainCarriage(dailyTrainCarriageSaveReq)) {
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
    public CommonRespond<Page<DailyTrainCarriageQueryResp>> query_list(@Valid DailyTrainCarriageQueryReq dailyTrainCarriageQueryReq) {

        Page<DailyTrainCarriageQueryResp> page = dailyTrainCarriageService.queryDailyTrainCarriages(dailyTrainCarriageQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (dailyTrainCarriageService.deleteById(id)){
            return CommonRespond.succeed("删除成功",true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }
}
