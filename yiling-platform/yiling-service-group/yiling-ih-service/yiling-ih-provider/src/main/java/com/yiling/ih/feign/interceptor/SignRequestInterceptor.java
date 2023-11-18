package com.yiling.ih.feign.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.AESUtils;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.SignUtils;
import com.yiling.ih.config.IHServiceConfig;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求签名拦截器
 *
 * @author: xuan.zhou
 * @date: 2022/6/15
 */
@Slf4j
public class SignRequestInterceptor implements RequestInterceptor {

    @Autowired
    IHServiceConfig ihServiceConfig;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (!ihServiceConfig.getSign()) {
            return;
        }

        String timestamp = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");

        Map<String, String> params = MapUtil.newHashMap();
        params.put(Constants.PARAM_APP_KEY, ihServiceConfig.getAppKey());
        params.put(Constants.PARAM_TIMESTAMP, timestamp);
        params.put(Constants.PARAM_QUERY_PARAMS, this.getQueryParams(requestTemplate));
        // Request Body
        String body = null;
        if (!requestTemplate.request().isBinary()) {
            body = StrUtil.str(requestTemplate.body(), Constants.CHARSET_UTF8);
            if (ihServiceConfig.getEncrypt()) {
                body = this.encryptBody(body);
            }
            params.put(Constants.PARAM_BODY, body);
        }

        // 计算签名
        String sign;
        try {
            sign = SignUtils.sign(params, ihServiceConfig.getAppSecret(), Constants.SIGN_METHOD_HMAC);
        } catch (IOException e) {
            sign = "Request signature error";
            log.error("生成请求签名出错: appKey={}, params={}", ihServiceConfig.getAppKey(), JSONUtil.toJsonStr(params), e);
        }

        requestTemplate
                .header(Constants.PARAM_APP_KEY, ihServiceConfig.getAppKey())
                .header(Constants.PARAM_TIMESTAMP, timestamp)
                .header(Constants.PARAM_SIGN, sign);

        if (StrUtil.isNotBlank(body)) {
            requestTemplate.body(body);
        }
    }

    private String encryptBody(String body) {
        if (StrUtil.isEmpty(body)) {
            return body;
        }

        return AESUtils.encryptHex(body, ihServiceConfig.getAppSecret());
    }

    private String getQueryParams(RequestTemplate requestTemplate) {
        Map<String, Collection<String>> queries = requestTemplate.queries();
        if (CollUtil.isEmpty(queries)) {
            return "";
        }

        String[] keys = queries.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String value = queries.get(key).stream().findFirst().orElse(null);
            if (StrUtil.isNotEmpty(key) && StrUtil.isNotEmpty(value)) {
                sb.append(key).append(value);
            }
        }

        return sb.toString();
    }
}
