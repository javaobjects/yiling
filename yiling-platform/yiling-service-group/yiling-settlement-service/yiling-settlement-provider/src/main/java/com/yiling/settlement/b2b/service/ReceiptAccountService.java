package com.yiling.settlement.b2b.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.b2b.dto.ReceiptAccountDTO;
import com.yiling.settlement.b2b.dto.request.QueryReceiptAccountPageListRequest;
import com.yiling.settlement.b2b.dto.request.SaveOrUpdateReceiptAccountRequest;
import com.yiling.settlement.b2b.entity.ReceiptAccountDO;

/**
 * <p>
 * b2b企业收款账户表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-27
 */
public interface ReceiptAccountService extends BaseService<ReceiptAccountDO> {

	/**
	 * 查询生效状态的企业收款账户
	 *
	 * @param eid
	 * @return
	 */
	ReceiptAccountDTO queryValidReceiptAccountByEid(Long eid);

	/**
	 * 查询生效状态的企业收款账户
	 *
	 * @param eidList
	 * @return
	 */
	List<ReceiptAccountDTO> queryValidReceiptAccountByEidList(List<Long> eidList);

	/**
	 * 企业收款账户提交审核
	 *
	 * @param request
	 * @return
	 */
	Boolean submitAuditReceiptAccount(SaveOrUpdateReceiptAccountRequest request);

	/**
	 * 分页查询有效状态的收款账户列表
	 *
	 * @param request
	 * @return
	 */
	Page<ReceiptAccountDTO> queryReceiptAccountPageList(QueryReceiptAccountPageListRequest request);
}
