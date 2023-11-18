package com.yiling.sjms.gateway.filter;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
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
import cn.hutool.json.JSONUtil;
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

        Long userId = Convert.toLong(data.getUserId(), 0L);
        if (userId == 0L) {
            Set<URI> uris = exchange.getAttributeOrDefault(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());
            String originalUrl = (uris.isEmpty()) ? "Unknown" : uris.iterator().next().getPath();
            log.warn("token信息不全：requestUrl={}, data={}", originalUrl, JSONUtil.toJsonStr(data));
        }

        ServerHttpRequest serverHttpRequest = request.mutate()
                .header(Constants.CURRENT_APP_ID, Convert.toStr(data.getAppId()))
                .header(Constants.CURRENT_USER_ID, Convert.toStr(data.getUserId()))
                .header(Constants.CURRENT_USER_CODE, Convert.toStr(data.getUserCode()))
                .header(Constants.CURRENT_USER_IP, IPUtils.getIp(exchange.getRequest())).build();
        ServerWebExchange serverWebExchange = exchange.mutate().request(serverHttpRequest).build();
        return chain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return 20;
    }
}
