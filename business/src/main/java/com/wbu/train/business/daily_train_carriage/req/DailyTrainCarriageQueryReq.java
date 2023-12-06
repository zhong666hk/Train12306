package com.wbu.train.business.daily_train_carriage.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wbu.train.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 乘车人
 * @TableName daily_train_carriage
 */
@Data
public class DailyTrainCarriageQueryReq extends PageReq {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String trainCode;

}