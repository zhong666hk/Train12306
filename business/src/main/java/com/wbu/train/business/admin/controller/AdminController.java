package com.wbu.train.business.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbu.train.business.admin.req.AdminLoginReq;
import com.wbu.train.business.admin.req.AdminQueryReq;
import com.wbu.train.business.admin.req.AdminRegisterReq;
import com.wbu.train.business.admin.req.AdminSaveReq;
import com.wbu.train.business.admin.resp.AdminQueryResp;
import com.wbu.train.business.admin.service.AdminService;
import com.wbu.train.common.Aspect.annotation.LogAnnotation;
import com.wbu.train.common.context.LoginAdminContext;
import com.wbu.train.common.exception.AppExceptionExample;
import com.wbu.train.common.respon.CommonRespond;
import com.wbu.train.common.respon.LoginResp;
import com.wbu.train.common.respon.RespondExample;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/admin")
public class AdminController {
    public static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;


    @LogAnnotation
    @PostMapping("/save")
    public CommonRespond<Boolean> register(@Valid @RequestBody AdminSaveReq adminSaveReq) {
        if (ObjectUtil.isEmpty(adminSaveReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        try {
            if (adminService.saveAdmin(adminSaveReq)) {
                return CommonRespond.succeed("乘客添加或修改成功！！！", true);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return CommonRespond.error(AppExceptionExample.PASSENGER_SAVE_ERROR);
        }
        return CommonRespond.error(AppExceptionExample.PASSENGER_SAVE_ERROR);
    }


    @LogAnnotation
    @GetMapping("/query_list")
    public CommonRespond<Page<AdminQueryResp>> query_list(@Valid AdminQueryReq adminQueryReq) {
        if (ObjectUtil.isNull(LoginAdminContext.getId())) {
            CommonRespond.error(AppExceptionExample.NOT_LOGIN);
        }
        LOG.info("管理员{}，调用接口query_list",LoginAdminContext.getId());
        Page<AdminQueryResp> page = adminService.queryAdmins(adminQueryReq);
        return CommonRespond.succeed(page);
    }

    @LogAnnotation
    @DeleteMapping("/delete/{id}")
    public CommonRespond<Boolean> delete(@PathVariable Long id) {
        if (ObjectUtil.isNull(LoginAdminContext.getId())) {
            CommonRespond.error(AppExceptionExample.NOT_LOGIN);
        }
        LOG.info("管理员{}，调用接口delete",LoginAdminContext.getId());
        if (adminService.deleteById(id)) {
            return CommonRespond.succeed("删除成功", true);
        }
        return CommonRespond.error(AppExceptionExample.ADMIN_DELETE_ERROR);
    }

    @LogAnnotation
    @PostMapping("/login")
    public CommonRespond<LoginResp> Login(@Valid @RequestBody AdminLoginReq adminLoginReq) {
        if (ObjectUtil.isNull(adminLoginReq)){
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        return adminService.login(adminLoginReq);

    }

    @LogAnnotation
    @PostMapping("/register")
    public CommonRespond<Long> register(@RequestBody @Valid AdminRegisterReq adminRegisterReq) {
        if (ObjectUtil.isEmpty(adminRegisterReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        return adminService.register(adminRegisterReq);
    }
}
