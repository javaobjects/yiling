package com.yiling.settlement.b2b.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.b2b.api.SettlementDetailApi;
import com.yiling.settlement.b2b.dto.SettlementDetailDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementDetailPageListRequest;
import com.yiling.settlement.b2b.service.SettlementDetailService;

/**
 * @author dexi.yao
 * @date 2021-10-25
 */
@DubboService
public class SettlementDetailApiImpl implements SettlementDetailApi {
	@Autowired
	SettlementDetailService settlementDetailService;

	@Override
	public Page<SettlementDetailDTO> querySettlementDetailPageList(QuerySettlementDetailPageListRequest request) {
		return settlementDetailService.querySettlementDetailPageList(request);
	}

	@Override
	public List<SettlementDetailDTO> querySettlementDetailByOrderId(Long orderId) {
		return settlementDetailService.querySettlementDetailByOrderId(orderId);
	}

	@Override
	public List<SettlementDetailDTO> querySettlementDetailBySettlementId(List<Long> settlementIdList) {
		return settlementDetailService.querySettlementDetailBySettlementId(settlementIdList);
	}


}
