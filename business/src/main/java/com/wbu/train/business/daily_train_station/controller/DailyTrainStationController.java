package com.wbu.train.business.daily_train_station.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.business.daily_train_station.domain.DailyTrainStation;
import com.wbu.train.business.daily_train_station.req.DailyTrainStationQueryReq;
import com.wbu.train.business.daily_train_station.req.DailyTrainStationSaveReq;
import com.wbu.train.business.daily_train_station.resp.DailyTrainStationQueryResp;
import com.wbu.train.business.daily_train_station.service.DailyTrainStationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/daily_train_station")
public class DailyTrainStationController {
    public static final Logger LOG = LoggerFactory.getLogger(DailyTrainStationController.class);

    @Autowired
    private DailyTrainStationService dailyTrainStationService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody DailyTrainStationSaveReq dailyTrainStationSaveReq) {
        if (ObjectUtil.isEmpty(dailyTrainStationSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try{
            if (dailyTrainStationService.saveDailyTrainStation(dailyTrainStationSaveReq)) {
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
    public CommonRespond<Page<DailyTrainStationQueryResp>> query_list(@Valid DailyTrainStationQueryReq dailyTrainStationQueryReq) {

        Page<DailyTrainStationQueryResp> page = dailyTrainStationService.queryDailyTrainStations(dailyTrainStationQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (dailyTrainStationService.deleteById(id)){
            return CommonRespond.succeed("删除成功",true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }
}
