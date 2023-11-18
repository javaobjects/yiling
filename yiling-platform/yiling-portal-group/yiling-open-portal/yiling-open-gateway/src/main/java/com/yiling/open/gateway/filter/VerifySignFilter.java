package com.yiling.open.gateway.filter;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;

import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.SignUtils;
import com.yiling.open.gateway.config.CommonUrlsConfig;
import com.yiling.open.gateway.config.IgnoreUrlsConfig;
import com.yiling.open.gateway.config.ServiceConfig;
import com.yiling.open.gateway.context.GatewayContext;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 验证签名过滤器
 *
 * @author xuan.zhou
 * @date 2021/5/18
 */
@Slf4j
@Component
@RefreshScope
public class VerifySignFilter implements GlobalFilter, Ordered {

    @Value("${spring.profiles.active}")
    private String env;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Autowired
    private CommonUrlsConfig commonUrlsConfig;
    @Autowired
    ServiceConfig serviceConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        Set<URI> uris = exchange.getAttributeOrDefault(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());
        String originalUrl = (uris.isEmpty()) ? "Unknown" : uris.iterator().next().getPath();

        // 验证原始请求路径是否为安全路径
        if (this.isIgnoreUrl(originalUrl)) {
            return chain.filter(exchange);
        }

        // 验证请求签名
        if (serviceConfig.getVerifySign()) {
            try {
                Result result = this.validateRequest(request, exchange);
                if (!result.isSuccessful()) {
                    return this.authError(response, result.getMessage());
                }
            } catch (IOException e) {
                log.error("验证请求出错：requestUrl={}", originalUrl, e);
                return this.authError(response, "验证请求出错");
            }
        }

        // 验证原始请求路径是否为公有权限路径
        if (this.isCommonUrl(originalUrl)) {
            return chain.filter(exchange);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 10;
    }

    private Result validateRequest(ServerHttpRequest request, ServerWebExchange exchange) throws IOException {
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);

        String appKey = request.getHeaders().getFirst(Constants.PARAM_APP_KEY);
        if (StrUtil.isBlank(appKey)) {
            return Result.failed("appKey不能为空");
        }

        ServiceConfig.AppInfo appInfo = serviceConfig.getAppInfo(appKey);
        if (appInfo == null) {
            return Result.failed("无效的appKey");
        }

        String timestamp = request.getHeaders().getFirst(Constants.PARAM_TIMESTAMP);
        if (StrUtil.isBlank(appKey)) {
            return Result.failed("请求时间戳不能为空");
        }

        if (serviceConfig.getVerifyReplay()) {
            Calendar calendar;
            try {
                calendar = DateUtil.parseByPatterns(timestamp, "yyyy-MM-dd HH:mm:ss");
            } catch (DateException e) {
                return Result.failed("请求时间戳格式错误");
            }

            Date expirationTime = DateUtil.offset(calendar.getTime(), DateField.SECOND, serviceConfig.getReplayTime());
            if (new Date().after(expirationTime)) {
                return Result.failed("请求已过期");
            }
        }

        String sign = request.getHeaders().getFirst(Constants.PARAM_SIGN);
        if (StrUtil.isBlank(sign)) {
            return Result.failed("请求签名不能为空");
        }

        Map<String, String> params = MapUtil.newHashMap();
        params.put(Constants.PARAM_APP_KEY, appKey);
        params.put(Constants.PARAM_TIMESTAMP, timestamp);
        params.put(Constants.PARAM_QUERY_PARAMS, this.getQueryParams(request));
        params.put(Constants.PARAM_BODY, gatewayContext.getBody());
        // 计算签名
        String mySign = SignUtils.sign(params, appInfo.getAppSecret(), Constants.SIGN_METHOD_HMAC);
        if (!mySign.equals(sign)) {
            return Result.failed("请求签名错误");
        }

        // 放入APP_KEY和APP_SECRET
        gatewayContext.setAppKey(appInfo.getAppKey());
        gatewayContext.setAppSecret(appInfo.getAppSecret());
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);

        return Result.success();
    }

    private String getQueryParams(ServerHttpRequest request) {
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        if (CollUtil.isEmpty(queryParams)) {
            return "";
        }

        String[] keys = queryParams.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String value = queryParams.getFirst(key);
            if (StrUtil.isNotEmpty(key) && StrUtil.isNotEmpty(value)) {
                sb.append(key).append(value);
            }
        }

        return sb.toString();
    }

    private Mono<Void> authError(ServerHttpResponse response, String message) {
        Result result = Result.unauthorized(message);
        DataBuffer buffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Flux.just(buffer));
    }

    private Mono<Void> forbiddenError(ServerHttpResponse response) {
        Result result = Result.forbidden();
        DataBuffer buffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Flux.just(buffer));
    }

    /**
     * 判断原始请求路径是否为安全路径
     *
     * @param originalUrl 原始请求路径
     * @return
     */
    private boolean isIgnoreUrl(String originalUrl) {
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String url : ignoreUrlsConfig.getUrls()) {
            if (pathMatcher.match(url, originalUrl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断原始请求路径是否为公有权限路径
     *
     * @param originalUrl 原始请求路径
     * @return
     */
    private boolean isCommonUrl(String originalUrl) {
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String url : commonUrlsConfig.getUrls()) {
            if (pathMatcher.match(url, originalUrl)) {
                return true;
            }
        }
        return false;
    }

}
