package com.wbu.train.business.confirm_order.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.business.confirm_order.req.ConfirmOrderDoReq;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.business.confirm_order.domain.ConfirmOrder;
import com.wbu.train.business.confirm_order.req.ConfirmOrderQueryReq;
import com.wbu.train.business.confirm_order.req.ConfirmOrderSaveReq;
import com.wbu.train.business.confirm_order.resp.ConfirmOrderQueryResp;
import com.wbu.train.business.confirm_order.service.ConfirmOrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/confirm_order")
public class ConfirmOrderController {
    public static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderController.class);

    @Autowired
    private ConfirmOrderService confirmOrderService;


    @LogAnnotation
    @PostMapping("/do")
    public CommonRespond<Object> register(@Valid @RequestBody ConfirmOrderDoReq req) {
        confirmOrderService.doConfirm(req);
        return CommonRespond.succeed("购票成功",null);
    }


    @LogAnnotation
    @GetMapping("/query_list")
    public CommonRespond<Page<ConfirmOrderQueryResp>> query_list(@Valid ConfirmOrderQueryReq confirmOrderQueryReq) {

        Page<ConfirmOrderQueryResp> page = confirmOrderService.queryConfirmOrders(confirmOrderQueryReq);
        return CommonRespond.succeed(page);
    }

}
