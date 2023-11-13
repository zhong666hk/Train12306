package com.wbu.train.getway.filter;

import com.wbu.train.getway.utile.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class LoginFilter implements GlobalFilter , Ordered {
    public static final Logger LOG = LoggerFactory.getLogger(LoginFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        // 排除不需要拦截的请求
        if (path.contains("/admin")
                ||path.contains("/reject")
                ||path.contains("/hello")
                ||path.contains("/member/sendCode")
                ||path.contains("/member/register")
                ||path.contains("/member/login")){
            LOG.info("不需要登录验证{}",path);
            return chain.filter(exchange);
        }

        LOG.info("需要登录验证{}",path);
        // 获取header的token
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (token==null || token.isEmpty()){
            LOG.info("token为空，请求拦截");
            return exchange.getResponse().setComplete();
        }

        // 校验token中的信息是否有效 ---1.是否改过 2.是否过期
        if (JwtUtil.validate(token)){
            LOG.info("token有效，放行");
            return chain.filter(exchange);
        }
        LOG.warn("token无效，请求拦截");
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
