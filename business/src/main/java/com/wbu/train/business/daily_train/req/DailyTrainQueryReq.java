package com.wbu.train.business.daily_train.req;

import com.wbu.train.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 乘车人
 * @TableName daily_train
 */
@Data
public class DailyTrainQueryReq extends PageReq {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String code;
}