package com.yiling.user.integral.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralGiveRuleApi;
import com.yiling.user.integral.bo.IntegralGiveRuleDetailBO;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.IntegralGiveRuleDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveMultipleConfigDTO;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMatchRuleRequest;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.SaveIntegralMultipleConfigRequest;
import com.yiling.user.integral.dto.request.SaveOrderGiveIntegralRequest;
import com.yiling.user.integral.dto.request.SaveSignPeriodRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.service.IntegralGiveRuleService;
import com.yiling.user.integral.service.IntegralOrderGiveConfigService;
import com.yiling.user.integral.service.IntegralOrderGiveMultipleConfigService;
import com.yiling.user.integral.service.IntegralSignPeriodService;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分发放规则 API 实现
 *
 * @author: lun.yu
 * @date: 2022-12-29
 */
@Slf4j
@DubboService
public class IntegralGiveRuleApiImpl implements IntegralGiveRuleApi {

    @Autowired
    IntegralGiveRuleService integralGiveRuleService;
    @Autowired
    IntegralSignPeriodService integralSignPeriodService;
    @Autowired
    IntegralOrderGiveConfigService integralOrderGiveConfigService;
    @Autowired
    IntegralOrderGiveMultipleConfigService integralOrderGiveMultipleConfigService;

    @Override
    public Page<IntegralRuleItemBO> queryListPage(QueryIntegralRulePageRequest request) {
        return integralGiveRuleService.queryListPage(request);
    }

    @Override
    public boolean updateStatus(UpdateRuleStatusRequest request) {
        return integralGiveRuleService.updateStatus(request);
    }

    @Override
    public IntegralGiveRuleDTO saveBasic(SaveIntegralRuleBasicRequest request) {
        return integralGiveRuleService.saveBasic(request);
    }

    @Override
    public boolean saveSignPeriod(SaveSignPeriodRequest request) {
        return integralSignPeriodService.saveSignPeriod(request);
    }

    @Override
    public boolean saveOrderGiveIntegral(SaveOrderGiveIntegralRequest request) {
        return integralOrderGiveConfigService.saveOrderGiveIntegral(request);
    }

    @Override
    public List<GenerateMultipleConfigDTO> generateMultipleConfig(Long giveRuleId) {
        return integralOrderGiveMultipleConfigService.generateMultipleConfig(giveRuleId);
    }

    @Override
    public boolean saveMultipleConfig(SaveIntegralMultipleConfigRequest request) {
        return integralOrderGiveMultipleConfigService.saveMultipleConfig(request);
    }

    @Override
    public IntegralGiveRuleDetailBO get(Long id) {
        return integralGiveRuleService.get(id);
    }

    @Override
    public IntegralGiveRuleDTO getById(Long id) {
        return PojoUtils.map(integralGiveRuleService.getById(id), IntegralGiveRuleDTO.class);
    }

    @Override
    public Long copy(Long id, Long opUserId) {
        return integralGiveRuleService.copy(id, opUserId);
    }

    @Override
    public List<IntegralGiveRuleDTO> getCurrentValidRule(Integer platform) {
        return integralGiveRuleService.getCurrentValidRule(platform);
    }

    @Override
    public GenerateMultipleConfigDTO autoMatchRule(QueryIntegralGiveMatchRuleRequest request) {
        return integralOrderGiveConfigService.autoMatchRule(request);
    }
}
