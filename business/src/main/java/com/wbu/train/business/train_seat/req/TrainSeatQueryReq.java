package com.wbu.train.business.train_seat.req;

import com.wbu.train.common.req.PageReq;
import lombok.Data;

/**
 * 乘车人
 * @TableName train_seat
 */
@Data
public class TrainSeatQueryReq extends PageReq {
        private String trainCode;
}