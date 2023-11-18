package com.yiling.payment.basic.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.payment.basic.dto.BankDTO;
import com.yiling.payment.basic.dto.request.QueryBankPageListRequest;

/**
 * @author dexi.yao
 * @date 2021-11-10
 */
public interface BankApi {

	/**
	 * 分页查询银行列表
	 *
	 * @param request
	 * @return
	 */
	Page<BankDTO> queryBankPageList(QueryBankPageListRequest request);

	/**
	 * 根据银行id查询银行列表
	 *
	 * @param idList
	 * @return
	 */
	List<BankDTO> queryByIdList(List<Long> idList);
}
