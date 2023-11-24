package com.wbu.train.business.admin.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wbu.train.business.admin.domain.Admin;
import com.wbu.train.business.admin.req.AdminLoginReq;
import com.wbu.train.business.admin.req.AdminQueryReq;
import com.wbu.train.business.admin.req.AdminRegisterReq;
import com.wbu.train.business.admin.req.AdminSaveReq;
import com.wbu.train.business.admin.resp.AdminQueryResp;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.LoginResp;

import java.util.List;

/**
* @author 钟正保
* @description 针对表【admin(乘车人)】的数据库操作Service
* @createDate 2023-11-14 14:43:47
*/
public interface AdminService extends IService<Admin> {
    public boolean saveAdmin(AdminSaveReq req);

    /**
     * 查询当前登录用户的购票
     * @param req
     * @return
     */
    public Page<AdminQueryResp> queryAdmins(AdminQueryReq req);

    /**
     * 删除admin 通过id
     * @param id
     */
    public boolean  deleteById(Long id);

    CommonRespond<LoginResp> login(AdminLoginReq adminLoginReq);

    CommonRespond<Long> register(AdminRegisterReq adminRegisterReq);
}