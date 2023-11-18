package com.yiling.basic.location.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.yiling.basic.location.service.IPLocationQueryService;
import com.yiling.framework.common.util.SpringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * IP归属地查询服务工厂类
 *
 * @author: xuan.zhou
 * @date: 2022/10/18
 */
@Slf4j
@Component
@RefreshScope
public class IPLocationQueryServiceFactory {

    @Value("${ip.location.query.serviceName}")
    private String serviceName;

    public IPLocationQueryService getService() {
        IPLocationQueryService service = (IPLocationQueryService) SpringUtils.getBean(serviceName);
        return service;
    }
}
