package com.wbu.train.common.context;

import com.wbu.train.common.respon.LoginResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginMemberContext {
    private static final Logger LOG= LoggerFactory.getLogger(LoginMemberContext.class);
    private static ThreadLocal<LoginResp> member=new ThreadLocal<>();
    public static LoginResp getLoginResp(){return member.get();}
    public static void setMember(LoginResp loginResp){LoginMemberContext.member.set(loginResp);}

    public static Long getId(){
        try {
            return member.get().getId();
        }catch (Exception e){
            LOG.error("获取登录会员信息失败",e.getMessage());
            throw e;
        }
    }
}
