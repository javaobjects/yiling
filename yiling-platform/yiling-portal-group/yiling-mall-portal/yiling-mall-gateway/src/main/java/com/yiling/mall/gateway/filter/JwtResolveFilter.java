package com.yiling.mall.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.JwtTokenUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * JWT解析过滤器
 *
 * @author: xuan.zhou
 * @date: 2021/7/16
 */
@Slf4j
@Component
public class JwtResolveFilter implements GlobalFilter, Ordered {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 获取Token
        String token = request.getHeaders().getFirst(tokenHeader);
        if (StrUtil.isEmpty(token) || !token.startsWith(this.tokenHead)) {
            return chain.filter(exchange);
        }

        // The part after "Bearer "
        String jwtToken = token.substring(tokenHead.length());
        JwtDataModel data = jwtTokenUtils.getDataFromToken(jwtToken);
        if (data == null) {
            return chain.filter(exchange);
        }

        ServerHttpRequest serverHttpRequest = request.mutate()
            .header(Constants.CURRENT_APP_ID, Convert.toStr(data.getAppId()))
            .header(Constants.CURRENT_USER_ID, Convert.toStr(data.getUserId()))
            .header(Constants.CURRENT_EID, Convert.toStr(data.getEid()))
            .header(Constants.CURRENT_USER_IP, IPUtils.getIp(exchange.getRequest()))
            .header(Constants.CURRENT_ETYPE, Convert.toStr(data.getEtype()))
            .header(Constants.CURRENT_CHANNEL_ID, Convert.toStr(data.getChannelId()))
            .header(Constants.CURRENT_EMPLOYEE_ID, Convert.toStr(data.getEmployeeId()))
            .header(Constants.CURRENT_IS_ADMIN, Convert.toStr(data.getAdminFlag())).build();
        ServerWebExchange serverWebExchange = exchange.mutate().request(serverHttpRequest).build();
        return chain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return 20;
    }
}
