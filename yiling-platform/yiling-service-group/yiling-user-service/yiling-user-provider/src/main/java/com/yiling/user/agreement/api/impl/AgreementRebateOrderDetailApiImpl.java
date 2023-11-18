package com.yiling.user.agreement.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreement.api.AgreementRebateOrderDetailApi;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.request.PageListByIdRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailPageListRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderPageListRequest;
import com.yiling.user.agreement.enums.AgreementRebateOrderCashStatusEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderConditionStatusEnum;
import com.yiling.user.agreement.service.AgreementRebateOrderDetailService;

/**
 * @author: shuang.zhang
 * @date: 2021/7/7
 */
@DubboService
public class AgreementRebateOrderDetailApiImpl implements AgreementRebateOrderDetailApi {
	@Autowired
	AgreementRebateOrderDetailService agreementRebateOrderDetailService;

	@Override
	public List<AgreementRebateOrderDetailDTO> queryAgreementRebateOrderDetailList(QueryRebateOrderDetailRequest request) {
		return agreementRebateOrderDetailService.queryAgreementRebateOrderDetailList(request);
	}

	@Override
	public Page<AgreementRebateOrderDetailDTO> queryAgreementRebateOrderDetailPageList(QueryRebateOrderDetailPageListRequest request) {
		return agreementRebateOrderDetailService.queryAgreementRebateOrderDetailPageList(request);
	}

	@Override
	public Page<AgreementRebateOrderDetailDTO> pageListById(PageListByIdRequest request) {
		return agreementRebateOrderDetailService.pageListById(request);
	}

	@Override
	public Page<AgreementRebateOrderDetailDTO> queryRebateOrderPageList(QueryRebateOrderPageListRequest request,
																		AgreementRebateOrderConditionStatusEnum conditionStatusEnum,
																		AgreementRebateOrderCashStatusEnum agreementRebateOrderCashStatusEnum) {
		return agreementRebateOrderDetailService.queryRebateOrderPageList(request, conditionStatusEnum, agreementRebateOrderCashStatusEnum);
	}
}
