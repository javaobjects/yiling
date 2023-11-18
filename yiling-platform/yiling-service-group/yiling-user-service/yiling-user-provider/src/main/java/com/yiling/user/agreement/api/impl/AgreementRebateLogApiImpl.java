package com.yiling.user.agreement.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.api.AgreementRebateLogApi;
import com.yiling.user.agreement.dto.AgreementRebateLogDTO;
import com.yiling.user.agreement.dto.request.AddRebateLogRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementRebateLogPageListRequest;
import com.yiling.user.agreement.entity.AgreementRebateLogDO;
import com.yiling.user.agreement.enums.AgreementCashTypeEnum;
import com.yiling.user.agreement.service.AgreementRebateLogService;
import com.yiling.user.agreement.service.AgreementRebateOrderService;

/**
 * @author: dexi.yao
 * @date: 2021/7/13
 */
@DubboService
public class AgreementRebateLogApiImpl implements AgreementRebateLogApi {

	@Autowired
	AgreementRebateLogService   rebateLogService;
	@Autowired
	AgreementRebateOrderService rebateOrderService;

	@Override
	public Map<String, BigDecimal> queryEntAccountDiscountAmount(List<String> accounts, AgreementCashTypeEnum cashTypeEnum) {
		return rebateLogService.queryEntAccountDiscountAmount(accounts, cashTypeEnum);
	}

	@Override
	public Map<String, List<AgreementRebateLogDTO>> queryEntAccountRebateList(List<String> accounts, AgreementCashTypeEnum cashTypeEnum) {
		List<AgreementRebateLogDO> logDOList = rebateLogService.queryEntAccountDiscountAmountList(accounts, cashTypeEnum);
		List<AgreementRebateLogDTO> list = PojoUtils.map(logDOList, AgreementRebateLogDTO.class);
		return list.stream().collect(Collectors.groupingBy(AgreementRebateLogDTO::getEasCode));
	}

	@Override
	public Page<AgreementRebateLogDTO> queryAgreementRebateLogPageList(QueryAgreementRebateLogPageListRequest request) {
		Page<AgreementRebateLogDTO> page = rebateLogService.queryAgreementRebateLogPageList(request);
		return page;
	}

	@Override
	public Boolean batchSave(List<AddRebateLogRequest> list) {
		return rebateLogService.batchSave(list);
	}
}
