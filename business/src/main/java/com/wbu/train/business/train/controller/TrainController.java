package com.wbu.train.business.train.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.business.train.req.TrainQueryReq;
import com.wbu.train.business.train.req.TrainSaveReq;
import com.wbu.train.business.train.resp.TrainQueryResp;
import com.wbu.train.business.train.service.TrainService;
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
@RequestMapping("/business/train")
public class TrainController {
    public  final Logger LOG = LoggerFactory.getLogger(TrainController.class);

    @Autowired
    private TrainService trainService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> save(@Valid @RequestBody TrainSaveReq trainSaveReq) {
        if (ObjectUtil.isEmpty(trainSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }

        if (trainService.saveTrain(trainSaveReq)) {
            return CommonRespond.succeed("车次添加或修改成功！！！", true);
        }

        return CommonRespond.error(AppExceptionExample.PASSENGER_SAVE_ERROR);
    }


    @LogAnnotation
    @GetMapping("/query_list")
    public CommonRespond<Page<TrainQueryResp>> query_list(@Valid TrainQueryReq trainQueryReq) {

        Page<TrainQueryResp> page = trainService.queryTrains(trainQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (trainService.deleteById(id)) {
            return CommonRespond.succeed("删除成功", true);
        }
        return CommonRespond.error(AppExceptionExample.TRAIN_DELETE_ERROR);
    }

    @LogAnnotation
    @GetMapping("/query_all")
    public CommonRespond<List<TrainQueryResp>> query_all() {
        List<TrainQueryResp> trainQueryRespList = trainService.queryAll();
        return CommonRespond.succeed(trainQueryRespList);
    }

    @LogAnnotation
    @GetMapping("hello")
    public String hello() {

        return "hello";
    }
}
