package com.yiling.user.integral.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.dto.request.QueryIntegralExchangeOrderPageRequest;
import com.yiling.user.integral.api.IntegralExchangeOrderApi;
import com.yiling.user.integral.bo.IntegralExchangeOrderItemBO;
import com.yiling.user.integral.dto.IntegralExchangeOrderDTO;
import com.yiling.user.integral.dto.IntegralExchangeOrderReceiptInfoDTO;
import com.yiling.user.integral.dto.request.UpdateExpressRequest;
import com.yiling.user.integral.dto.request.UpdateIntegralExchangeOrderRequest;
import com.yiling.user.integral.dto.request.UpdateReceiptAddressRequest;
import com.yiling.user.integral.service.IntegralExchangeOrderReceiptInfoService;
import com.yiling.user.integral.service.IntegralExchangeOrderService;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分兑换订单 API 实现
 *
 * @author: lun.yu
 * @date: 2023-01-11
 */
@Slf4j
@DubboService
public class IntegralExchangeOrderApiImpl implements IntegralExchangeOrderApi {

    @Autowired
    IntegralExchangeOrderService integralExchangeOrderService;
    @Autowired
    IntegralExchangeOrderReceiptInfoService integralExchangeOrderReceiptInfoService;

    @Override
    public Page<IntegralExchangeOrderItemBO> queryListPage(QueryIntegralExchangeOrderPageRequest request) {
        return integralExchangeOrderService.queryListPage(request);
    }

    @Override
    public boolean exchange(UpdateIntegralExchangeOrderRequest request) {
        return integralExchangeOrderService.exchange(request);
    }

    @Override
    public IntegralExchangeOrderDTO getById(Long id) {
        return PojoUtils.map(integralExchangeOrderService.getById(id), IntegralExchangeOrderDTO.class);
    }

    @Override
    public IntegralExchangeOrderReceiptInfoDTO getReceiptInfoByOrderId(Long orderId) {
        return integralExchangeOrderReceiptInfoService.getByExchangeOrderId(orderId);
    }

    @Override
    public boolean updateAddress(UpdateReceiptAddressRequest request) {
        return integralExchangeOrderReceiptInfoService.updateAddress(request);
    }

    @Override
    public boolean atOnceExchange(UpdateExpressRequest request) {
        return integralExchangeOrderService.atOnceExchange(request);
    }
}
