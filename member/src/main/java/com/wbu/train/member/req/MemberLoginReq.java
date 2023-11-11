package com.wbu.train.member.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginReq {
    /**
     * @NotEmpty 用在集合类上面
     * @NotBlank 用在String上面
     * @NotNull 用在基本类型上
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$" ,message = "手机号不符合规范(1[3-9]XXX)")
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    public String code;
}
