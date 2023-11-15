package com.wbu.train.member.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.LoginResp;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.common.util.BusinessType;
import com.wbu.train.common.util.JwtUtil;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.member.domain.CodeInformation;
import com.wbu.train.member.domain.Member;
import com.wbu.train.member.mapper.MemberMapper;
import com.wbu.train.member.req.MemberLoginReq;
import com.wbu.train.member.req.MemberRegisterReq;
import com.wbu.train.member.req.MemberSendCodeReq;
import com.wbu.train.member.service.CodeInformationService;
import com.wbu.train.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【member(会员)】的数据库操作Service实现
 * @createDate 2023-11-09 15:57:21
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    @Autowired
    private CodeInformationService codeInformationService;

    @Override
    public CommonRespond<Long> register(MemberRegisterReq memberRegisterReq) {
        //为空返回
        if (ObjectUtil.isEmpty(memberRegisterReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        String mobile = memberRegisterReq.getMobile();
        //手机号的验证  前台+后台 -->    @Pattern(regexp = "^1[3-9]\\d{9}$" ,message = "手机号不符合规范(1[3-9]XXX)")
//        boolean matches = mobile.matches("^1[3-9]\\d{9}$");
//        if (!matches){
//            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
//        }
        //手机号已经注册过了
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile", mobile);
        List<Member> list = this.list(memberQueryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new MyException(AppExceptionExample.MEMBER_MOBILE_HAS_EXIST);
        }
        Member member = new Member();
        member.setMobile(mobile);
        member.setId(SnowUtil.getSnowflakeNextId());
        boolean saveResult = this.save(member);
        if (saveResult) {
            return CommonRespond.succeed("注册成功", member.getId());
        }
        return new CommonRespond<>(500, "注册失败");
    }

    @Override
    public CommonRespond<String> sendCode(MemberSendCodeReq memberRegisterReq) {
        //为空返回
        if (ObjectUtil.isEmpty(memberRegisterReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        String mobile = memberRegisterReq.getMobile();
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile", mobile);
        List<Member> list = this.list(memberQueryWrapper);
        // 手机号不存在就创建并且发送验证码 存在就直接发送验证码
        if (CollectionUtil.isEmpty(list)) {
            Member member = new Member();
            member.setMobile(mobile);
            member.setId(SnowUtil.getSnowflakeNextId());
            // 注册失败就抛出异常
            if (!this.save(member)) {
                return new CommonRespond<>(500, "获取验证码失败");
            }
        }
        // 获取验证码
        String code = RandomUtil.randomString(4);
        // 保存短信记录表: 手机号，短信验证码，有效期，是否已经使用，业务类型，创建时间，使用时间
        CodeInformation codeInformation = new CodeInformation();
        codeInformation.setBusinessType(BusinessType.TYPE_LOGIN.getType());
        codeInformation.setCode(code);
        codeInformation.setMobile(mobile);
        if (!codeInformationService.save(codeInformation)){
            throw new MyException(10006,"codeInformationService插入异常");
        }
        //TODO 对接短信通道

        return CommonRespond.succeed(code);
    }

    @Override
    public CommonRespond<LoginResp> login(MemberLoginReq memberLoginReq) {
        // 根据这个信息去查codeInformation的消息 看验证码是否过期是否可以用
        String code = memberLoginReq.getCode();
        String mobile = memberLoginReq.getMobile();
        QueryWrapper<CodeInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",code).eq("mobile",mobile);
        CodeInformation codeInformation = codeInformationService.getOne(queryWrapper);
        //  未查到信息-->是逻辑删除  -->手机和验证码早就已经验证过了-->验证码已经使用过
        if (ObjectUtil.isNull(codeInformation)){
            throw new MyException(AppExceptionExample.MEMBER_CODE_HAS_USED);
        }
        // 如果验证码已经过期
        Date expirationTime = codeInformation.getExpirationTime();
        if (DateUtil.compare(expirationTime,new Date())<0){
            throw new MyException(AppExceptionExample.MEMBER_CODE_EXPIRE);
        }

        // 如果验证码类型不匹配
        if (!codeInformation.getBusinessType().equals(BusinessType.TYPE_LOGIN.getType())){
            throw new MyException(AppExceptionExample.MEMBER_CODE_TYPE_ERROR);
        }
        // 通过校验使用
        codeInformation.setUseTime(new Date());
        // -->更新使用时间
        if (!codeInformationService.updateById(codeInformation)) {
            throw new MyException(AppExceptionExample.SYSTEM_INNER_ERROR);
        }
        // 标记已经使用过
        if (!codeInformationService.removeById(codeInformation.getCodeId())) {
            throw new MyException(AppExceptionExample.SYSTEM_INNER_ERROR);
        }
        //生成token
        Member member = this.query().select("id").eq("mobile", mobile).one();
        String token = JwtUtil.createToken(member.getId(), mobile);
        return CommonRespond.succeed("登陆成功",new LoginResp(true,token));
    }
}




