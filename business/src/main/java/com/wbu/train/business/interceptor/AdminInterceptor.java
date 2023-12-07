package com.wbu.train.business.interceptor;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.wbu.train.common.context.LoginAdminContext;
import com.wbu.train.common.respon.AdminLoginResp;
import com.wbu.train.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截信息 --->将登录的token信息解析放到ThreadLocal中
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final Logger LOG= LoggerFactory.getLogger(AdminInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 设置日志的自定义参数--流水号
        MDC.put("LOG_ID",System.currentTimeMillis()+ RandomUtil.randomString(3));
        String token=request.getHeader("token");
        LOG.info("token为{}",token);
        JSONObject jsonObject = JwtUtil.getJSONObject(token);
        AdminLoginResp adminLoginResp = jsonObject.toBean(AdminLoginResp.class);
        LOG.warn("当前登录会员{}",adminLoginResp);
        // 设置当前会员的参数
        LoginAdminContext.setAdmin(adminLoginResp);
        return true;
    }

}
