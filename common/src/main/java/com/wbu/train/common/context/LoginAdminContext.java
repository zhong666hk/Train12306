package com.wbu.train.common.context;

import com.wbu.train.common.respon.AdminLoginResp;
import com.wbu.train.common.respon.LoginResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginAdminContext {
    private static final Logger LOG= LoggerFactory.getLogger(LoginAdminContext.class);
    private static ThreadLocal<AdminLoginResp> admin=new ThreadLocal<>();
    public static AdminLoginResp getAdminLoginResp(){return admin.get();}
    public static void setAdmin(AdminLoginResp adminLoginResp){
        LoginAdminContext.admin.set(adminLoginResp);}

    public static Long getId(){
        try {
            return admin.get().getId();
        }catch (Exception e){
            LOG.error("获取登录会员信息失败",e.getMessage());
            throw e;
        }
    }
}
