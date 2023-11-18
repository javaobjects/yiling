package com.yiling.order.settlement.api.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.settlement.api.SettlementApi;
import com.yiling.order.settlement.dto.SettlementFullDTO;
import com.yiling.order.settlement.dto.request.SaveSettlementRequest;
import com.yiling.order.settlement.service.SettlementService;

/**
 * @author: shuang.zhang
 * @date: 2021/8/10
 */
@DubboService
public class SettlementApiImpl implements SettlementApi {

    @Autowired
    SettlementService settlementService;

    @Override
    public Boolean saveSettlement(SaveSettlementRequest request) {
        return settlementService.saveSettlement(request);
    }

    @Override
    public List<SettlementFullDTO> getSettlementDetailByEidAndTime(QueryOrderUseAgreementRequest request) {
        return settlementService.getSettlementDetailByEidAndTime(request);
    }

    @Override
    public BigDecimal getsettlementAmountByOrderId(Long orderId) {
        return settlementService.getsettlementAmountByOrderId(orderId);
    }
}
