package com.wbu.train.business.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbu.train.business.admin.req.AdminLoginReq;
import com.wbu.train.business.admin.req.AdminRegisterReq;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.exception.MyException;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.LoginResp;
import com.wbu.train.common.respon.RespondExample;
import com.wbu.train.common.util.JwtUtil;
import com.wbu.train.common.util.SnowUtil;
import com.wbu.train.business.admin.domain.Admin;
import com.wbu.train.business.admin.mapper.AdminMapper;
import com.wbu.train.business.admin.req.AdminQueryReq;
import com.wbu.train.business.admin.req.AdminSaveReq;
import com.wbu.train.business.admin.resp.AdminQueryResp;
import com.wbu.train.business.admin.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 钟正保
 * @description 针对表【admin(管理员)】的数据库操作Service实现
 * @createDate 2023-11-14 14:43:47
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
        implements AdminService {

    @Override
    public boolean saveAdmin(AdminSaveReq req) {
        DateTime date = DateUtil.date(); // hutool的是已经格式化了的
        if (ObjectUtil.isNull(req)) {
            return false;
        }
        // 拷贝类
        Admin admin = BeanUtil.copyProperties(req, Admin.class);
        // 如果是id为空--->说明是添加的操作
        if (ObjectUtil.isNull(admin.getId())){
            admin.setId(SnowUtil.getSnowflakeNextId());
            admin.setCreateTime(date);
            return this.save(admin);
        }// id不为空说明是修改的操作
        else {
            return this.updateById(admin);
        }
    }

    @Override
    public Page<AdminQueryResp> queryAdmins(AdminQueryReq req) {
        // ObjectUtil.isNotNull(req)为空是管理员来查询所有的票
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        //原理会对第一个sql进行拦截 添加limit
//        PageHelper.startPage( req.getPage(),req.getSize());
        Page<Admin> page = this.page(new Page<>(req.getPage(), req.getSize()), adminQueryWrapper);
        Page<AdminQueryResp> adminQueryRespPage = new Page<>();
        BeanUtil.copyProperties(page,adminQueryRespPage);
        return adminQueryRespPage;
    }

    @Override
    public boolean deleteById(Long id) {
        if (ObjectUtil.isNull(id)){
            return false;
        }
        return this.removeById(id);
    }

    @Override
    public CommonRespond<LoginResp> login(AdminLoginReq adminLoginReq) {
        String mobile = adminLoginReq.getMobile();
        String password=adminLoginReq.getPassword();
        //生成token
        Admin admin = this.query().select("id").eq("mobile", mobile).eq("password",password).one();
        if (ObjectUtil.isNull(admin)){
            return CommonRespond.error(AppExceptionExample.ADMIN_NOT_EXIST);
        }
        String token = JwtUtil.createToken(admin.getId(), mobile);
        return CommonRespond.succeed("登陆成功",new LoginResp(true,token));
    }

    @Override
    public CommonRespond<Long> register(AdminRegisterReq adminRegisterReq) {
        //为空返回
        if (ObjectUtil.isEmpty(adminRegisterReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        String mobile = adminRegisterReq.getMobile();
        String password = adminRegisterReq.getPassword();
        //手机号已经注册过了
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("mobile", mobile).eq("password",password);
        List<Admin> list = this.list(adminQueryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new MyException(AppExceptionExample.ADMIN_HAS_EXIST);
        }
        Admin newAdmin = new Admin();
        newAdmin.setMobile(mobile);
        newAdmin.setPassword(password);
        newAdmin.setId(SnowUtil.getSnowflakeNextId());
        boolean saveResult = this.save(newAdmin);
        if (saveResult) {
            return CommonRespond.succeed("注册成功", newAdmin.getId());
        }
        return new CommonRespond<>(500, "注册失败");
    }

}




