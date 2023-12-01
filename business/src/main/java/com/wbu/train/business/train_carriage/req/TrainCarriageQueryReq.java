package com.wbu.train.business.train_carriage.req;

import com.wbu.train.common.req.PageReq;
import lombok.Data;

/**
 * 乘车人
 * @TableName train_carriage
 */
@Data
public class TrainCarriageQueryReq extends PageReq {
    private String trainCode;
}