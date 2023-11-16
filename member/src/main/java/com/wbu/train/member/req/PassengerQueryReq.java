package com.wbu.train.member.req;

import com.wbu.train.common.req.PageReq;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

/**
 * 乘车人
 * @TableName passenger
 */
@Data
public class PassengerQueryReq extends PageReq {
    private Long memberId;
}