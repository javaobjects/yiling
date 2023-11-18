package com.yiling.settlement.b2b.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.b2b.api.ReceiptAccountApi;
import com.yiling.settlement.b2b.dto.ReceiptAccountDTO;
import com.yiling.settlement.b2b.dto.request.QueryReceiptAccountPageListRequest;
import com.yiling.settlement.b2b.dto.request.SaveOrUpdateReceiptAccountRequest;
import com.yiling.settlement.b2b.dto.request.UpdateReceiptAccountRequest;
import com.yiling.settlement.b2b.entity.ReceiptAccountDO;
import com.yiling.settlement.b2b.service.ReceiptAccountService;

/**
 * @author dexi.yao
 * @date 2021-10-27
 */
@DubboService
public class ReceiptAccountApiImpl implements ReceiptAccountApi {

	@Autowired
	ReceiptAccountService receiptAccountService;


	@Override
	public ReceiptAccountDTO queryValidReceiptAccountByEid(Long eid) {
		return receiptAccountService.queryValidReceiptAccountByEid(eid);
	}

	@Override
	public List<ReceiptAccountDTO> queryValidReceiptAccountByEidList(List<Long> eidList) {
		return receiptAccountService.queryValidReceiptAccountByEidList(eidList);
	}

	@Override
	public ReceiptAccountDTO queryBiId(Long id) {
		ReceiptAccountDO accountDO = receiptAccountService.getById(id);
		return PojoUtils.map(accountDO, ReceiptAccountDTO.class);
	}

	@Override
	public Boolean submitAuditReceiptAccount(SaveOrUpdateReceiptAccountRequest request) {
		return receiptAccountService.submitAuditReceiptAccount(request);
	}

	@Override
	public Page<ReceiptAccountDTO> queryReceiptAccountPageList(QueryReceiptAccountPageListRequest request) {
		return receiptAccountService.queryReceiptAccountPageList(request);
	}

	@Override
	public Boolean updateById(UpdateReceiptAccountRequest request) {
		ReceiptAccountDO accountDO = PojoUtils.map(request, ReceiptAccountDO.class);
		return receiptAccountService.updateById(accountDO);
	}
}
