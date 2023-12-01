package com.wbu.train.business.train_station.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.business.train_station.req.TrainStationQueryReq;
import com.wbu.train.business.train_station.req.TrainStationSaveReq;
import com.wbu.train.business.train_station.resp.TrainStationQueryResp;
import com.wbu.train.business.train_station.service.TrainStationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/train_station")
public class TrainStationController {
    public static final Logger LOG = LoggerFactory.getLogger(TrainStationController.class);

    @Autowired
    private TrainStationService trainStationService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody TrainStationSaveReq trainStationSaveReq) {
        if (ObjectUtil.isEmpty(trainStationSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }

        if (trainStationService.saveTrainStation(trainStationSaveReq)) {
            return CommonRespond.succeed("火车车站添加或修改成功！！！",true);
        }

        return CommonRespond.error(AppExceptionExample.PASSENGER_SAVE_ERROR);
    }


    @LogAnnotation
    @GetMapping("/query_list")
    public CommonRespond<Page<TrainStationQueryResp>> query_list(@Valid TrainStationQueryReq trainStationQueryReq) {

        Page<TrainStationQueryResp> page = trainStationService.queryTrainStations(trainStationQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (trainStationService.deleteById(id)){
            return CommonRespond.succeed("删除成功",true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }
}
