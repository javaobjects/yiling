package com.yiling.payment.basic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.payment.basic.dto.BankDTO;
import com.yiling.payment.basic.dto.request.QueryBankPageListRequest;
import com.yiling.payment.basic.entity.BankDO;

/**
 * <p>
 * 银行表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-11-10
 */
public interface BankService extends BaseService<BankDO> {

	/**
	 * 分页查询银行列表
	 *
	 * @param request
	 * @return
	 */
	Page<BankDTO> queryBankPageList(QueryBankPageListRequest request);
}
