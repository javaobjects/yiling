package com.yiling.mall.settlement.b2b.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.settlement.b2b.B2bSettlementApi;
import com.yiling.mall.settlement.b2b.service.SettlementService;

/**
 * @author dexi.yao
 * @date 2021-10-19
 */
@DubboService
public class B2bSettlementApiImpl implements B2bSettlementApi {
	@Autowired
	SettlementService settlementService;

	@Override
	public Boolean submitPayment(List<Long> settlementIdList, Long opUserId) {
		return settlementService.submitPayment(settlementIdList, opUserId);
	}


}
