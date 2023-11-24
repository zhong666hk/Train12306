package com.wbu.train.business.config;

import com.wbu.train.business.interceptor.AdminInterceptor;
import com.wbu.train.common.interceptor.MemberInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {
    // 必需要注入到容器中的。
    @Resource
    AdminInterceptor adminInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login");
    }
}
