package com.yiling.open.gateway.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import com.yiling.open.gateway.context.GatewayContext;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 网关上下文过滤器
 * 
 * @author xuan.zhou
 * @date 2022/6/16
 */
@Slf4j
@Component
public class GatewayContextFilter extends BaseFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().pathWithinApplication().value();

        GatewayContext gatewayContext = new GatewayContext();
        gatewayContext.setPath(path);
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);

        HttpHeaders headers = request.getHeaders();
        MediaType contentType = headers.getContentType();
        long contentLength = headers.getContentLength();
        if (contentLength > 0L) {
            if (contentType != null && contentType.includes(MediaType.APPLICATION_JSON)) {
                return this.readBody(exchange, chain, gatewayContext);
            }
            if (contentType != null && contentType.includes(MediaType.APPLICATION_FORM_URLENCODED)) {
                return this.readFormData(exchange, chain, gatewayContext);
            }
        }

        log.debug("[GatewayContext] ContentType:{}, Gateway context is set with {}", contentType, gatewayContext);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> readFormData(ServerWebExchange exchange, GatewayFilterChain chain, GatewayContext gatewayContext) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return exchange.getFormData()
                .doOnNext(multiValueMap -> {
                    gatewayContext.setFormData(multiValueMap);
                    log.debug("[GatewayContext] Read FormData:{}", multiValueMap);
                })
                .then(Mono.defer(() -> {
                    Charset charset = headers.getContentType().getCharset();
                    charset = charset == null ? StandardCharsets.UTF_8 : charset;
                    String charsetName = charset.name();
                    MultiValueMap<String, String> formData = gatewayContext.getFormData();
                    if (formData == null || formData.isEmpty()) {
                        return chain.filter(exchange);
                    }

                    StringBuilder formDataBodyBuilder = new StringBuilder();
                    String entryKey;
                    List<String> entryValue;
                    try {
                        for (Map.Entry<String, List<String>> entry : formData.entrySet()) {
                            entryKey = entry.getKey();
                            entryValue = entry.getValue();
                            if (entryValue.size() > 1) {
                                for (String value : entryValue) {
                                    formDataBodyBuilder.append(entryKey).append("=").append(URLEncoder.encode(value, charsetName)).append("&");
                                }
                            } else {
                                formDataBodyBuilder.append(entryKey).append("=").append(URLEncoder.encode(entryValue.get(0), charsetName)).append("&");
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                        // 忽略
                    }

                    String formDataBodyString = "";
                    if (formDataBodyBuilder.length() > 0) {
                        formDataBodyString = formDataBodyBuilder.substring(0, formDataBodyBuilder.length() - 1);
                    }

                    byte[] bodyBytes = formDataBodyString.getBytes(charset);
                    ServerHttpRequestDecorator decorator = this.decorate(exchange, headers, bodyBytes);
                    ServerWebExchange mutateExchange = exchange.mutate().request(decorator).build();
                    log.debug("[GatewayContext] Rewrite Form Data:{}", formDataBodyString);
                    return chain.filter(mutateExchange);
                }));
    }

    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, GatewayContext gatewayContext) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    DataBufferUtils.retain(dataBuffer);

                    Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));

                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return cachedFlux;
                        }
                    };

                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

                    return ServerRequest.create(mutatedExchange, messageReaders)
                            .bodyToMono(String.class)
                            .doOnNext(objectValue -> {
                                gatewayContext.setBody(objectValue);
                                log.debug("[GatewayContext] Read JsonBody:{}", objectValue);
                            }).then(chain.filter(mutatedExchange));
                });
    }
}
