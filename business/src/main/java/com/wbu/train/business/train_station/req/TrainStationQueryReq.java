package com.wbu.train.business.train_station.req;

import com.wbu.train.common.req.PageReq;
import lombok.Data;

/**
 * 乘车人
 * @TableName train_station
 */
@Data
public class TrainStationQueryReq extends PageReq {
    private String trainCode;
}