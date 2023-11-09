package com.wbu.train.member.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.member.domain.Member;
import com.wbu.train.member.mapper.MemberMapper;
import com.wbu.train.member.req.MemberRegisterReq;
import com.wbu.train.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 钟正保
* @description 针对表【member(会员)】的数据库操作Service实现
* @createDate 2023-11-09 15:57:21
*/
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper,Member> implements MemberService{
    @Override
    public CommonRespond<Boolean> register(MemberRegisterReq memberRegisterReq) {
        //为空返回
        if (ObjectUtil.isEmpty(memberRegisterReq)){
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        //手机号的验证  前台+后台
        String mobile = memberRegisterReq.getMobile();
        boolean matches = mobile.matches("^1[3-9]\\d{9}$");
        if (!matches){
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        //手机号已经注册过了
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile", mobile);
        List<Member> list = this.list(memberQueryWrapper);
        if (CollectionUtil.isNotEmpty(list)){
            throw new  MyException(AppExceptionExample.MEMBER_MOBILE_HAS_EXIST);
        }
        Member member = new Member();
        member.setMobile(mobile);
        boolean saveResult = this.save(member);
        if (saveResult){
          return CommonRespond.succeed("注册成功",saveResult);
        }
        return new CommonRespond<>(500, "注册失败", saveResult);
    }
}




