package com.yiling.user.integral.api.impl;

import org.apache.dubbo.config.annotation.DubboService;

import com.yiling.user.integral.api.IntegralApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分 API 实现
 *
 * @author: lun.yu
 * @date: 2021/10/20
 */
@Slf4j
@DubboService
public class IntegralApiImpl implements IntegralApi {

    @Deprecated
    @Override
    public Integer getEnterpriseIntegral(Long eid) {
        return 0;
    }

}
