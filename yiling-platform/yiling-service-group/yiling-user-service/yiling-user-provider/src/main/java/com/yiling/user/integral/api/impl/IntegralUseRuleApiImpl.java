package com.yiling.user.integral.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralUseRuleApi;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.bo.IntegralUseRuleDetailBO;
import com.yiling.user.integral.dto.IntegralLotteryConfigDTO;
import com.yiling.user.integral.dto.IntegralUseRuleDTO;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralLotteryConfigRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.UpdateIntegralExchangeLotteryRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.service.IntegralLotteryConfigService;
import com.yiling.user.integral.service.IntegralUseRuleService;

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
    @Autowired
    IntegralLotteryConfigService integralLotteryConfigService;

    @Override
    public Page<IntegralRuleItemBO> queryListPage(QueryIntegralRulePageRequest request) {
        return integralUseRuleService.queryListPage(request);
    }

    @Override
    public boolean updateStatus(UpdateRuleStatusRequest request) {
        return integralUseRuleService.updateStatus(request);
    }

    @Override
    public IntegralUseRuleDTO saveBasic(SaveIntegralRuleBasicRequest request) {
        return integralUseRuleService.saveBasic(request);
    }

    @Override
    public IntegralUseRuleDTO getById(Long id) {
        return PojoUtils.map(integralUseRuleService.getById(id), IntegralUseRuleDTO.class);
    }

    @Override
    public Long copy(Long id, Long opUserId) {
        return integralUseRuleService.copy(id, opUserId);
    }

    @Override
    public boolean saveLotteryConfig(SaveIntegralLotteryConfigRequest request) {
        return integralLotteryConfigService.saveLotteryConfig(request);
    }

    @Override
    public IntegralLotteryConfigDTO getRuleByLotteryActivityId(Long lotteryActivityId) {
        return integralLotteryConfigService.getRuleByLotteryActivityId(lotteryActivityId);
    }

    @Override
    public boolean exchangeLotteryTimes(UpdateIntegralExchangeLotteryRequest request) {
        return integralUseRuleService.exchangeLotteryTimes(request);
    }
}
