package com.yiling.marketing.integral.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.marketing.integral.api.IntegralUseRuleApi;
import com.yiling.marketing.integral.service.IntegralUseRuleService;
import com.yiling.user.integral.bo.IntegralUseRuleDetailBO;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分消耗规则 API 实现
 *
 * @author: lun.yu
 * @date: 2023-01-06
 */
@Slf4j
@DubboService
public class IntegralUseRuleApiImpl implements IntegralUseRuleApi {

    @Autowired
    IntegralUseRuleService integralUseRuleService;

    @Override
    public IntegralUseRuleDetailBO get(Long id) {
        return integralUseRuleService.get(id);
    }

}
