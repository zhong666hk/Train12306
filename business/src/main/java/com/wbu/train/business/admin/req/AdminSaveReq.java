package com.wbu.train.business.admin.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class AdminSaveReq {

    /**
     * id
     */
    private Long id;

    /**
     * 手机号
     */
    @NotBlank(message = "【手机】不能为空")
    private String mobile;

    /**
     * 密码
     */
    @NotBlank(message = "【密码】不能为空")
    private String password;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 是否删除(0：没有删除1：已经删除)
     */
    private Integer isDelete;

}
