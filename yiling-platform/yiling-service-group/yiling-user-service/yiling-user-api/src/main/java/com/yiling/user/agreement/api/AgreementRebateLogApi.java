package com.yiling.user.agreement.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreement.dto.AgreementRebateLogDTO;
import com.yiling.user.agreement.dto.request.AddRebateLogRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementRebateLogPageListRequest;
import com.yiling.user.agreement.enums.AgreementCashTypeEnum;

/**
 * @author: dexi.yao
 * @date: 2021/7/13
 */
public interface AgreementRebateLogApi {

	/**
	 * 根据企业账号查询各账号下以兑付金额
	 *
	 * @param accounts
	 * @param cashTypeEnum 对付类型（为空则查询全部）
	 * @return key=账号 value=总额
	 */
	Map<String, BigDecimal> queryEntAccountDiscountAmount(List<String> accounts, AgreementCashTypeEnum cashTypeEnum);

	/**
	 * 批量查询企业账号下的以兑付记录
	 *
	 * @param accounts     账号列表
	 * @param cashTypeEnum 对付类型（为空则查询全部）
	 * @return key=账号 value=返利记录
	 */
	Map<String, List<AgreementRebateLogDTO>> queryEntAccountRebateList(List<String> accounts, AgreementCashTypeEnum cashTypeEnum);

	/**
	 * 分页查询兑付日志
	 *
	 * @param request
	 * @return
	 */
	Page<AgreementRebateLogDTO> queryAgreementRebateLogPageList(QueryAgreementRebateLogPageListRequest request);

	/**
	 * 批量添加返利日志
	 *
	 * @param list
	 * @return
	 */
	Boolean batchSave(List<AddRebateLogRequest> list);

}
