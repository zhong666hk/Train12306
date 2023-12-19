package com.wbu.train.business.feign;

import com.wbu.train.business.config.FeignConfiguration;
import com.wbu.train.common.req.MemberTicketSaveReq;
import com.wbu.train.common.respon.CommonRespond;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="business",url = "http://127.0.0.1:8001/api/member",configuration = FeignConfiguration.class) //单一指定的不需要注册中心和负载均衡
public interface MemberFeign {
    @PostMapping("/feign/ticket/admin/save")
    CommonRespond<Boolean> saveTicket(@Valid @RequestBody MemberTicketSaveReq ticketSaveReq);
}
