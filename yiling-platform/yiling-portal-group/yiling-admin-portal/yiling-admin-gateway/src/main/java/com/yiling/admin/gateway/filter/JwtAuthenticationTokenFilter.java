package com.yiling.admin.gateway.filter;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
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
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;

import com.yiling.admin.gateway.config.CommonUrlsConfig;
import com.yiling.admin.gateway.config.IgnoreUrlsConfig;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.JwtTokenUtils;
import com.yiling.user.system.api.MenuApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.MenuDTO;
import com.yiling.user.system.enums.MenuTypeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * JWT认证过滤器
 *
 * @author xuan.zhou
 * @date 2021/5/18
 */
@Slf4j
@Component
@RefreshScope
public class JwtAuthenticationTokenFilter implements GlobalFilter, Ordered {

    @Value("${jwt.tokenHeader}")
    private String           tokenHeader;
    @Value("${jwt.tokenHead}")
    private String           tokenHead;
    @Value("${secure.account.repeat-login:false}")
    private Boolean          accountRepeatLogin;
    @Value("${secure.request.verify-url:true}")
    private Boolean          requestVerifyUrl;
    @Value("${spring.profiles.active}")
    private String           env;

    @Autowired
    private JwtTokenUtils    jwtTokenUtils;
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Autowired
    private CommonUrlsConfig commonUrlsConfig;
    @Autowired
    private RedisService     redisService;

    @DubboReference
    MenuApi menuApi;

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

        // 开发、测试环境允许swagger接口方式访问
        if (Constants.DEBUG_ENV_LIST.contains(env)) {
            String currentUserId = request.getQueryParams().getFirst(CurrentAdminInfo.CURRENT_USER_ID);
            if (StrUtil.isNotEmpty(currentUserId)) {
                return chain.filter(exchange);
            }
        }

        // 获取Token
        String token = request.getHeaders().getFirst(tokenHeader);
        if (StrUtil.isEmpty(token)) {
            return this.authError(response, "您还未登录，请先登录");
        }

        // The part after "Bearer "
        String jwtToken = StrUtil.subAfter(token, tokenHead, false);
        // 这里做补充处理，理论上不应该出现这种情况
        if (StrUtil.isEmpty(jwtToken)) {
            log.error("token格式错误：token={}, requestUrl={}", token, originalUrl);
            return this.authError(response, "您还未登录，请先登录");
        }

        JwtDataModel data = jwtTokenUtils.getDataFromToken(jwtToken);
        if (data == null) {
            return this.authError(response, "会话已过期，请重新登录");
        }

        // 验证服务端Token
        boolean verifyServerTokenResult = this.verifyServerToken(jwtToken, data.getUserId());
        if (!verifyServerTokenResult) {
            return this.authError(response, "会话已过期，请重新登录");
        }

        // 验证原始请求路径是否为公有权限路径
        if (this.isCommonUrl(originalUrl)) {
            return chain.filter(exchange);
        }

        // 验证url
        boolean verifyRequestUrlResult = this.verifyRequestUrl(data.getUserId(), originalUrl);
        if (!verifyRequestUrlResult) {
            return this.forbiddenError(response);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 10;
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
     * 校验服务端Token
     *
     * @param jwtToken token字符串
     * @param userId 用户ID
     * @return
     */
    private boolean verifyServerToken(String jwtToken, Long userId) {
        if (accountRepeatLogin) {
            return true;
        }

        // 服务端保存的用户Token
        String userAppTokenKey = RedisKey.generate("token", AppEnum.ADMIN.getAppCode(), "user", userId.toString());
        if (!redisService.hasKey(userAppTokenKey)) {
            return false;
        }

        String userAppToken = (String) redisService.get(userAppTokenKey);
        if (!jwtToken.equals(userAppToken)) {
            return false;
        }

        return true;
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

    private boolean verifyRequestUrl(Long userId, String requestUrl) {
        if (!requestVerifyUrl) {
            return true;
        }

        List<String> urls = CollUtil.newArrayList();

        // 用户所有的菜单和按钮
        // TODO-LEO 需要将接口请求地址都配置到按钮上后，再转换为只取按钮
        List<MenuDTO> userMenus = menuApi.listMenusByUser(PermissionAppEnum.ADMIN, MenuTypeEnum.all(), 0L, userId);
        // 获取配置了url的菜单和按钮
        userMenus = userMenus.stream().filter(e -> StrUtil.isNotEmpty(e.getMenuUrl())).collect(Collectors.toList());

        for (MenuDTO userMenu : userMenus) {
            urls.addAll(StrUtil.splitTrim(userMenu.getMenuUrl(), ","));
        }

        urls = urls.stream().distinct().collect(Collectors.toList());

        PathMatcher pathMatcher = new AntPathMatcher();
        for (String url : urls) {
            if (pathMatcher.match(url, requestUrl)) {
                return true;
            }
        }

        return false;
    }
}
