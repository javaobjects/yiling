package com.yiling.settlement.b2b.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.b2b.api.SettlementOrderApi;
import com.yiling.settlement.b2b.dto.SettlementOrderDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementOrderPageListRequest;
import com.yiling.settlement.b2b.dto.request.UpdateSettlementStatusRequest;
import com.yiling.settlement.b2b.enums.SettlementOperationTypeEnum;
import com.yiling.settlement.b2b.service.SettlementOrderService;

/**
 * @author dexi.yao
 * @date 2021-10-26
 */
@DubboService
public class SettlementOrderApiImpl implements SettlementOrderApi {

	@Autowired
	SettlementOrderService settlementOrderService;


	@Override
	public Page<SettlementOrderDTO> querySettlementOrderPageList(QuerySettlementOrderPageListRequest request) {
		return settlementOrderService.querySettlementOrderPageList(request);
	}

	@Override
	public Boolean updateSettlementStatus(List<UpdateSettlementStatusRequest> successList, List<UpdateSettlementStatusRequest> failList, SettlementOperationTypeEnum type) {
		return settlementOrderService.updateSettlementStatus(successList, failList, type);
	}

    @Override
    public SettlementOrderDTO querySettOrderByOrderId(Long orderId) {
        return settlementOrderService.querySettOrderByOrderId(orderId);
    }
}
