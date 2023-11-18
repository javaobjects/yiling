package com.yiling.payment.basic.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.payment.basic.api.BankApi;
import com.yiling.payment.basic.dto.BankDTO;
import com.yiling.payment.basic.dto.request.QueryBankPageListRequest;
import com.yiling.payment.basic.entity.BankDO;
import com.yiling.payment.basic.service.BankService;

/**
 * @author dexi.yao
 * @date 2021-11-10
 */
@DubboService
public class BankApiImpl implements BankApi {

	@Autowired
	BankService bankService;

	@Override
	public Page<BankDTO> queryBankPageList(QueryBankPageListRequest request) {
		return bankService.queryBankPageList(request);
	}

	@Override
	public List<BankDTO> queryByIdList(List<Long> idList) {
		List<BankDO> bankDOS = bankService.listByIds(idList);
		return PojoUtils.map(bankDOS, BankDTO.class);
	}
}
