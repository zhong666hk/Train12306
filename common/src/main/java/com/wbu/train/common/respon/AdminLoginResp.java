package com.wbu.train.common.respon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginResp {
    private boolean loginState;
    private String token;
    private Long id;
    private String mobile;
    private String password;

    public AdminLoginResp(boolean loginState, String token) {
        this.loginState = loginState;
        this.token = token;
    }
}
