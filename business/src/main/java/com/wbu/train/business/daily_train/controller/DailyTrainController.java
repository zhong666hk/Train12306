package com.wbu.train.business.daily_train.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.business.daily_train.domain.DailyTrain;
import com.wbu.train.business.daily_train.req.DailyTrainQueryReq;
import com.wbu.train.business.daily_train.req.DailyTrainSaveReq;
import com.wbu.train.business.daily_train.resp.DailyTrainQueryResp;
import com.wbu.train.business.daily_train.service.DailyTrainService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/business/daily_train")
public class DailyTrainController {
    public static final Logger LOG = LoggerFactory.getLogger(DailyTrainController.class);

    @Autowired
    private DailyTrainService dailyTrainService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody DailyTrainSaveReq dailyTrainSaveReq) {
        if (ObjectUtil.isEmpty(dailyTrainSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try{
            if (dailyTrainService.saveDailyTrain(dailyTrainSaveReq)) {
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
    public CommonRespond<Page<DailyTrainQueryResp>> query_list(@Valid DailyTrainQueryReq dailyTrainQueryReq) {

        Page<DailyTrainQueryResp> page = dailyTrainService.queryDailyTrains(dailyTrainQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (dailyTrainService.deleteById(id)){
            return CommonRespond.succeed("删除成功",true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }

    @LogAnnotation
    @GetMapping("/gen-daily/{date}")
    public CommonRespond<Boolean> gen(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        if (dailyTrainService.genDaily(date)){
            return CommonRespond.succeed("自动生成日常车次信息成功",true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }
}
