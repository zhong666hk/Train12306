package com.wbu.train.confirm_order.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.context.LoginMemberContext;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.confirm_order.domain.Confirm_order;
import com.wbu.train.confirm_order.req.Confirm_orderQueryReq;
import com.wbu.train.confirm_order.req.Confirm_orderSaveReq;
import com.wbu.train.confirm_order.resp.Confirm_orderQueryResp;
import com.wbu.train.confirm_order.service.Confirm_orderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/confirm_order")
public class Confirm_orderController {
    public static final Logger LOG = LoggerFactory.getLogger(Confirm_orderController.class);

    @Autowired
    private Confirm_orderService confirm_orderService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody Confirm_orderSaveReq confirm_orderSaveReq) {
        if (ObjectUtil.isEmpty(confirm_orderSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try{
            if (confirm_orderService.saveConfirm_order(confirm_orderSaveReq)) {
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
    public CommonRespond<Page<Confirm_orderQueryResp>> query_list(@Valid Confirm_orderQueryReq confirm_orderQueryReq) {

        Page<Confirm_orderQueryResp> page = confirm_orderService.queryConfirm_orders(confirm_orderQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (confirm_orderService.deleteById(id)){
            return CommonRespond.succeed("删除成功",true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }
}
