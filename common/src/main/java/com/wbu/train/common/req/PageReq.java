package com.wbu.train.common.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageReq {
    @NotNull(message = "页码不能为空")
    @Min(value = 1,message = "当前页码最小为1")
    private Integer page;
    @NotNull(message = "页数不能为空")
    @Max(value = 100,message = "每页不能超过100条数据")
    private Integer size;
}
