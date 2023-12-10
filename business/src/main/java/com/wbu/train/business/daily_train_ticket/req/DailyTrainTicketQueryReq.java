package com.wbu.train.business.daily_train_ticket.req;

import com.wbu.train.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 乘车人
 * @TableName daily_train_ticket
 */
@Data
public class DailyTrainTicketQueryReq extends PageReq {
    private String trainCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private  String start;

    private String end;

}