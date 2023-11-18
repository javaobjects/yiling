package com.yiling.hmc.tencent.interceptor;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.yiling.hmc.config.TencentServiceConfig;
import com.yiling.hmc.tencent.utils.GenerateUserSignature;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 请求签名拦截器
 *
 * @author: xuan.zhou
 * @date: 2022/6/15
 */
@Slf4j
public class TencentRequestInterceptor implements feign.RequestInterceptor {

    @Autowired
    TencentServiceConfig tencentServiceConfig;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String uri = "?sdkappid={}&identifier={}&usersig={}&random={}&contenttype=json";
        String signature = GenerateUserSignature.GenTLSSignature(tencentServiceConfig.getTencentIMAppId(), tencentServiceConfig.getAdminUser(), GenerateUserSignature.EXPIRETIME, null, tencentServiceConfig.getTencentIMSecretKey());
        uri = StrUtil.format(uri, tencentServiceConfig.getTencentIMAppId(), tencentServiceConfig.getAdminUser(), signature, RandomUtil.randomNumbers(10));
        requestTemplate.uri(uri, true);
    }

}
