package com.yiling.user.agreement.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.dto.AgreementRebateLogDTO;
import com.yiling.user.agreement.dto.request.AddRebateLogRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementRebateLogPageListRequest;
import com.yiling.user.agreement.entity.AgreementRebateLogDO;
import com.yiling.user.agreement.enums.AgreementCashTypeEnum;

/**
 * <p>
 * 协议兑付日志表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-07-12
 */
public interface AgreementRebateLogService extends BaseService<AgreementRebateLogDO> {

	/**
	 * 批量查询企业账号下的以兑付额度
	 * @param accounts		账号列表
	 * @param cashTypeEnum  对付类型（为空则查询全部）
	 * @return
	 */
	Map<String, BigDecimal> queryEntAccountDiscountAmount(List<String> accounts, AgreementCashTypeEnum cashTypeEnum);

	/**
	 * 批量查询企业账号下的以兑付记录
	 * @param accounts
	 * @param cashTypeEnum
	 * @return
	 */
	List<AgreementRebateLogDO> queryEntAccountDiscountAmountList(List<String> accounts, AgreementCashTypeEnum cashTypeEnum);

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
