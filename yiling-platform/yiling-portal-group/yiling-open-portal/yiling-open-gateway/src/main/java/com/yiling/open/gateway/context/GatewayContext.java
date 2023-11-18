package com.yiling.open.gateway.context;

import org.springframework.util.MultiValueMap;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 网关上下文
 *
 * @author xuan.zhou
 * @date 2022/6/16
 */
@Getter
@Setter
@ToString
public class GatewayContext implements java.io.Serializable {

    private static final long serialVersionUID = 7681385789537442755L;

    public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";

    private String path;

    private String appKey;

    private String appSecret;

    private String body;

    private MultiValueMap<String, String> formData;

}
