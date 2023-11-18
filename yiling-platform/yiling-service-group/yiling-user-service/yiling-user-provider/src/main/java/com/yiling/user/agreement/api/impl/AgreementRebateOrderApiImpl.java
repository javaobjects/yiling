package com.yiling.user.agreement.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreement.api.AgreementRebateOrderApi;
import com.yiling.user.agreement.dto.AgreementOrderStatisticalDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDTO;
import com.yiling.user.agreement.dto.RebateOrderPageListDTO;
import com.yiling.user.agreement.dto.request.AgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.AgreementRebateOrderRequest;
import com.yiling.user.agreement.dto.request.CashAgreementRequest;
import com.yiling.user.agreement.dto.request.ClearAgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementRebateRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderPageListRequest;
import com.yiling.user.agreement.enums.AgreementRebateOrderCashStatusEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderConditionStatusEnum;
import com.yiling.user.agreement.service.AgreementRebateOrderService;

/**
 * @author: shuang.zhang
 * @date: 2021/7/7
 */
@DubboService
public class AgreementRebateOrderApiImpl implements AgreementRebateOrderApi {

    @Autowired
    AgreementRebateOrderService agreementRebateOrderService;

    @Override
    public Boolean saveBatch(List<AgreementRebateOrderRequest> agreementOrderList) {
        return agreementRebateOrderService.saveBatch(agreementOrderList);
    }

    @Override
    public List<AgreementRebateOrderDTO> getAgreementRebateOrderListByAgreementIds(QueryAgreementRebateRequest request) {
        return agreementRebateOrderService.getAgreementRebateOrderListByAgreementIds(request);
    }

    @Override
    public Boolean updateBatchDiscountAmount(AgreementConditionCalculateRequest request) {
        return agreementRebateOrderService.updateBatchDiscountAmount(request);
    }

    @Override
    public Boolean clearDiscountAmountByOrderIdsAndAgreementIds(ClearAgreementConditionCalculateRequest request) {
        return agreementRebateOrderService.clearDiscountAmountByOrderIdsAndAgreementIds(request);
    }

    @Override
    public Boolean cashAgreementByAgreementId(CashAgreementRequest request) {
        return agreementRebateOrderService.cashAgreementByAgreementId(request);
    }

	@Override
	public AgreementOrderStatisticalDTO statisticsOrderCount(Long agreementId) {
		return agreementRebateOrderService.statisticsOrderCount(agreementId);
	}

	@Override
	public Page<RebateOrderPageListDTO> queryRebateOrderPageList(QueryRebateOrderPageListRequest request,
																 AgreementRebateOrderConditionStatusEnum conditionStatusEnum,
																 AgreementRebateOrderCashStatusEnum agreementRebateOrderCashStatusEnum ) {
		return agreementRebateOrderService.queryRebateOrderPageList(request,conditionStatusEnum,agreementRebateOrderCashStatusEnum);
	}
}
