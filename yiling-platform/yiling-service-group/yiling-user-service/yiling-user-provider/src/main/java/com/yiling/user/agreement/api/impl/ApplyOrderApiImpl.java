package com.yiling.user.agreement.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.api.ApplyOrderApi;
import com.yiling.user.agreement.dto.ApplyOrderDTO;
import com.yiling.user.agreement.dto.request.AddApplyOrderRequest;
import com.yiling.user.agreement.dto.request.QueryApplyOrderPageListRequest;
import com.yiling.user.agreement.entity.ApplyOrderDO;
import com.yiling.user.agreement.service.ApplyOrderService;

/**
 * @author dexi.yao
 * @date 2021-07-30
 */
@DubboService
public class ApplyOrderApiImpl implements ApplyOrderApi {

	@Autowired
	ApplyOrderService applyOrderService;


	@Override
	public List<ApplyOrderDTO> queryApplyOrderList(List<Long> applyIdList) {
		return applyOrderService.queryApplyOrderList(applyIdList);
	}

	@Override
	public Page<ApplyOrderDTO> queryApplyOrderPageList(QueryApplyOrderPageListRequest request) {
		return applyOrderService.queryApplyOrderPageList(request);
	}

	@Override
	public List<ApplyOrderDTO> batchSaveOrUpdate(List<AddApplyOrderRequest> requests) {
		return applyOrderService.batchSaveOrUpdate(requests);
	}

	@Override
	public ApplyOrderDTO queryById(Long id) {
		ApplyOrderDO applyOrderDO = applyOrderService.getById(id);
		return PojoUtils.map(applyOrderDO, ApplyOrderDTO.class);
	}
}
