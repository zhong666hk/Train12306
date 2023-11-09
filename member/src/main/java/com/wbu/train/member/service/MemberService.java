package com.wbu.train.member.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.member.domain.Member;
import com.wbu.train.member.req.MemberRegisterReq;

/**
 * @author 钟正保
 * @description 针对表【member(会员)】的数据库操作Service
 * @createDate 2023-11-09 15:57:21
 */
public interface MemberService extends IService<Member> {
    /**
     * 注册会员
     *
     * @param memberRegisterReq 手机号
     * @return 返回注册是否成功
     */
    public CommonRespond<Boolean> register(MemberRegisterReq memberRegisterReq);
}
