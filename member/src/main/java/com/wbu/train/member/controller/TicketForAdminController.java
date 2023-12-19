package com.wbu.train.member.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.req.MemberTicketSaveReq;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.member.req.TicketQueryReq;
import com.wbu.train.member.resp.TicketQueryResp;
import com.wbu.train.member.service.TicketService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 给admin用的接口
 */
@RestController
@RequestMapping("/member/ticket/admin")
public class TicketForAdminController {
    public static final Logger LOG = LoggerFactory.getLogger(TicketForAdminController.class);

    @Autowired
    private TicketService ticketService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody MemberTicketSaveReq ticketSaveReq) {
        if (ObjectUtil.isEmpty(ticketSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try {
            if (ticketService.saveTicket(ticketSaveReq)) {
                return CommonRespond.succeed("乘客添加或修改成功！！！", true);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return CommonRespond.error(AppExceptionExample.PASSENGER_SAVE_ERROR);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_SAVE_ERROR);
    }


    @LogAnnotation
    @GetMapping("/query_list")
    public CommonRespond<Page<TicketQueryResp>> query_list(@Valid TicketQueryReq ticketQueryReq) {
        Page<TicketQueryResp> page = ticketService.queryTickets(ticketQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (ticketService.deleteById(id)) {
            return CommonRespond.succeed("删除成功", true);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_DELETE_ERROR);
    }
}
