package com.yiling.open.gateway.filter;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import com.yiling.framework.common.util.AESUtils;
import com.yiling.open.gateway.config.IgnoreUrlsConfig;
import com.yiling.open.gateway.config.ServiceConfig;
import com.yiling.open.gateway.context.GatewayContext;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 解密RequestBody过滤器
 *
 * @author: xuan.zhou
 * @date: 2021/7/16
 */
@Slf4j
@Component
public class DecryptBodyFilter extends BaseFilter implements GlobalFilter, Ordered {

    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    @Autowired
    ServiceConfig serviceConfig;
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

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

        if (!serviceConfig.getDecryptBody()) {
            return chain.filter(exchange);
        }

        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);

        HttpMethod method = request.getMethod();
        if (method != HttpMethod.POST) {
            // 非post请求不处理
            return chain.filter(exchange);
        }

        long contentLength = request.getHeaders().getContentLength();
        if (contentLength == 0L) {
            // 无内容不处理
            return chain.filter(exchange);
        }

        MediaType contentType = request.getHeaders().getContentType();
        if (contentType == null || !contentType.includes(MediaType.APPLICATION_JSON)) {
            // 非Json数据请求不处理
            return chain.filter(exchange);
        }

        Mono<String> descryptedBody = ServerRequest.create(exchange, messageReaders)
                .bodyToMono(String.class)
                .flatMap(body -> {
                    String newBody = this.descryptBody(body, gatewayContext.getAppSecret());
                    log.debug("[DecryptBodyFilter] Decrypted Body:{}", newBody);
                    gatewayContext.setBody(newBody);
                    return Mono.just(newBody);
                });

        BodyInserter bodyInserter = BodyInserters.fromPublisher(descryptedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);

        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    ServerHttpRequest decorator = this.decorate(exchange, headers, outputMessage);
                    return chain.filter(exchange.mutate().request(decorator).build());
                }));
    }

    @Override
    public int getOrder() {
        return 20;
    }

    private String descryptBody(String body, String secret) {
        return AESUtils.decryptStr(body, secret);
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

}
