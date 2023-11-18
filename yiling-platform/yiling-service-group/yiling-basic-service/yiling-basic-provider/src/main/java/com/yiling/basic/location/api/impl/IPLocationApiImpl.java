package com.yiling.basic.location.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.location.api.IPLocationApi;
import com.yiling.basic.location.bo.IPLocationBO;
import com.yiling.basic.location.service.IPLocationService;

/**
 * IP归属地 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/10/19
 */
@DubboService
public class IPLocationApiImpl implements IPLocationApi {

    @Autowired
    private IPLocationService ipLocationService;

    @Override
    public IPLocationBO query(String ip) {
        return ipLocationService.query(ip);
    }

    @Override
    public void check() {
        ipLocationService.check();
    }
}
