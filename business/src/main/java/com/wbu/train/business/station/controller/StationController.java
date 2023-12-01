package com.wbu.train.business.station.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.business.station.req.StationQueryReq;
import com.wbu.train.business.station.req.StationSaveReq;
import com.wbu.train.business.station.resp.StationQueryResp;
import com.wbu.train.business.station.service.StationService;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/station")
public class StationController {
    public static final Logger LOG = LoggerFactory.getLogger(StationController.class);

    @Autowired
    private StationService stationService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody StationSaveReq stationSaveReq) {
        if (ObjectUtil.isEmpty(stationSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }

        if (stationService.saveStation(stationSaveReq)) {
            return CommonRespond.succeed("添加/修改站台信息成功！！！", true);
        }
        return CommonRespond.error(AppExceptionExample.STATION_SAVE_ERROR);
    }


    @LogAnnotation
    @GetMapping("/query_list")
    public CommonRespond<Page<StationQueryResp>> query_list(@Valid StationQueryReq stationQueryReq) {
        Page<StationQueryResp> page = stationService.queryStations(stationQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (stationService.deleteById(id)) {
            return CommonRespond.succeed("删除成功", true);
        }
        return CommonRespond.error(AppExceptionExample.STATION_DELETE_ERROR);
    }

    @LogAnnotation
    @GetMapping("/query_all")
    public CommonRespond<List<StationQueryResp>> query_all() {
        List<StationQueryResp> stationQueryRespList = stationService.queryAll();
        return CommonRespond.succeed(stationQueryRespList);
    }
}
