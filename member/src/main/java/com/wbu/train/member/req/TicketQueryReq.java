package com.wbu.train.member.req;

import com.wbu.train.common.req.PageReq;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 乘车人
 * @TableName ticket
 */
@Data
public class TicketQueryReq extends PageReq {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}