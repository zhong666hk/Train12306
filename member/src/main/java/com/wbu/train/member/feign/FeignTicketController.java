package com.wbu.train.member.feign;

import cn.hutool.core.util.ObjectUtil;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.req.MemberTicketSaveReq;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.member.service.TicketService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member/feign/ticket")
public class FeignTicketController {
    @Autowired
    private TicketService ticketService;
    public static final Logger LOG = LoggerFactory.getLogger(FeignTicketController.class);

    /**
     * 给远程调用的接口
     * @param memberTicketSaveReq
     */
    @PostMapping("/save")
    public CommonRespond<Boolean> saveTicket(@Valid @RequestBody MemberTicketSaveReq memberTicketSaveReq) {
        if (ObjectUtil.isEmpty(memberTicketSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        if (ticketService.saveTicket(memberTicketSaveReq)) {
            return CommonRespond.succeed("车票添加或修改成功！！！",true);
        }
        throw new MyException(40000,"车票信息保存异常");
    }
}
