package com.yiling.settlement.b2b.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.b2b.api.SettlementApi;
import com.yiling.settlement.b2b.dto.SettlementAmountInfoDTO;
import com.yiling.settlement.b2b.dto.SettlementDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementPageListRequest;
import com.yiling.settlement.b2b.dto.request.SubmitSalePaymentRequest;
import com.yiling.settlement.b2b.dto.request.UpdatePaymentStatusRequest;
import com.yiling.settlement.b2b.dto.request.UpdateSettlementByIdRequest;
import com.yiling.settlement.b2b.entity.SettlementDO;
import com.yiling.settlement.b2b.service.SettlementService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * @author dexi.yao
 * @date 2021-10-19
 */
@DubboService
public class SettlementApiImpl implements SettlementApi {
	@Autowired
	SettlementService settlementService;

	@Override
	public void generateSettlement() {
		settlementService.generateSettlement(null);
	}

	@Override
	public void generateSettlementByEid(Long sellerEid) {
		settlementService.generateSettlement(sellerEid);
	}

	@Override
	public SettlementAmountInfoDTO querySettlementAmountInfo(Long eid) {
		return settlementService.querySettlementAmountInfo(eid);
	}

	@Override
	public SettlementDTO getById(Long id) {
		SettlementDO settlementDO = settlementService.getById(id);
		return PojoUtils.map(settlementDO, SettlementDTO.class);
	}

	@Override
	public Boolean updateById(UpdateSettlementByIdRequest request) {
		SettlementDO settlementDO = PojoUtils.map(request, SettlementDO.class);
		return settlementService.updateById(settlementDO);
	}

	@Override
	public List<SettlementDTO> getByIdList(List<Long> idList) {
		if (CollUtil.isEmpty(idList)) {
			return ListUtil.toList();
		}
		List<SettlementDO> settlementList = settlementService.listByIds(idList);
		return PojoUtils.map(settlementList, SettlementDTO.class);
	}

	@Override
	public Page<SettlementDTO> querySettlementPageList(QuerySettlementPageListRequest request) {
		return settlementService.querySettlementPageList(request);
	}

	@Override
	public Boolean updatePaymentStatus(List<UpdatePaymentStatusRequest> list) {
		return settlementService.updatePaymentStatus(list);
	}

	@Override
	public Boolean updateSettlementById(List<UpdateSettlementByIdRequest> requests) {
		if (CollUtil.isEmpty(requests)) {
			return Boolean.TRUE;
		}
		List<SettlementDO> settlementDOList = PojoUtils.map(requests, SettlementDO.class);
		return settlementService.updateSettlementById(settlementDOList);
	}

	@Override
	public Boolean submitSalePayment(SubmitSalePaymentRequest request) {
		return settlementService.submitSalePayment(request);
	}



}
