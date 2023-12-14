package com.wbu.train.business.confirm_order.req;


import com.wbu.train.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
public class ConfirmOrderQueryReq extends PageReq {
    private String trainCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
