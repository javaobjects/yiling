package com.yiling.marketing.integralmessage.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.integral.api.IntegralUseRuleApi;
import com.yiling.marketing.integral.service.IntegralUseRuleService;
import com.yiling.marketing.integralmessage.api.IntegralExchangeMessageConfigApi;
import com.yiling.marketing.integralmessage.dto.IntegralExchangeMessageConfigDTO;
import com.yiling.marketing.integralmessage.dto.request.DeleteIntegralMessageRequest;
import com.yiling.marketing.integralmessage.dto.request.QueryIntegralMessageListRequest;
import com.yiling.marketing.integralmessage.dto.request.QueryIntegralMessagePageRequest;
import com.yiling.marketing.integralmessage.dto.request.SaveIntegralExchangeMessageConfigRequest;
import com.yiling.marketing.integralmessage.dto.request.UpdateIntegralMessageOrderRequest;
import com.yiling.marketing.integralmessage.dto.request.UpdateIntegralMessageStatusRequest;
import com.yiling.marketing.integralmessage.service.IntegralExchangeMessageConfigService;
import com.yiling.user.integral.bo.IntegralUseRuleDetailBO;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分兑换消息配置 API 实现
 *
 * @author: lun.yu
 * @date: 2023-01-16
 */
@Slf4j
@DubboService
public class IntegralExchangeMessageConfigApiImpl implements IntegralExchangeMessageConfigApi {

    @Autowired
    IntegralExchangeMessageConfigService integralExchangeMessageConfigService;

    @Override
    public Page<IntegralExchangeMessageConfigDTO> queryListPage(QueryIntegralMessagePageRequest request) {
        return integralExchangeMessageConfigService.queryListPage(request);
    }

    @Override
    public List<IntegralExchangeMessageConfigDTO> queryList(QueryIntegralMessageListRequest request) {
        return integralExchangeMessageConfigService.queryList(request);
    }

    @Override
    public boolean saveConfig(SaveIntegralExchangeMessageConfigRequest request) {
        return integralExchangeMessageConfigService.saveConfig(request);
    }

    @Override
    public boolean deleteConfig(DeleteIntegralMessageRequest request) {
        return integralExchangeMessageConfigService.deleteConfig(request);
    }

    @Override
    public boolean updateStatus(UpdateIntegralMessageStatusRequest request) {
        return integralExchangeMessageConfigService.updateStatus(request);
    }

    @Override
    public boolean updateOrder(UpdateIntegralMessageOrderRequest request) {
        return integralExchangeMessageConfigService.updateOrder(request);
    }

    @Override
    public IntegralExchangeMessageConfigDTO get(Long id) {
        return PojoUtils.map(integralExchangeMessageConfigService.getById(id), IntegralExchangeMessageConfigDTO.class);
    }
}
