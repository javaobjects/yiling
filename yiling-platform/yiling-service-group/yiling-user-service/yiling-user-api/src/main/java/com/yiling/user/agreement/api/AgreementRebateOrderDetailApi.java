package com.yiling.user.agreement.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.request.PageListByIdRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailPageListRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderPageListRequest;
import com.yiling.user.agreement.enums.AgreementRebateOrderCashStatusEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderConditionStatusEnum;

/**
 * @author: shuang.zhang
 * @date: 2021/7/7
 */
public interface AgreementRebateOrderDetailApi {


	/**
	 * 根据订单id查询返利订单明细
	 *
	 * @param request
	 * @return
	 */
	List<AgreementRebateOrderDetailDTO> queryAgreementRebateOrderDetailList(QueryRebateOrderDetailRequest request);

	/**
	 * 根据订单id分页查询返利订单明细
	 *
	 * @param request
	 * @return
	 */
	Page<AgreementRebateOrderDetailDTO> queryAgreementRebateOrderDetailPageList(QueryRebateOrderDetailPageListRequest request);

	/**
	 * 根据主键id分页查询订单明细
	 *
	 * @param request
	 * @return
	 */
	Page<AgreementRebateOrderDetailDTO> pageListById(PageListByIdRequest request);

	/**
	 * 根据条件枚举分页查询返利订单
	 *
	 * @param request
	 * @param conditionStatusEnum                是否满足兑付=null时不限制
	 * @param agreementRebateOrderCashStatusEnum 是否兑付=null时不限制
	 * @return
	 */
	Page<AgreementRebateOrderDetailDTO> queryRebateOrderPageList(QueryRebateOrderPageListRequest request,
																 AgreementRebateOrderConditionStatusEnum conditionStatusEnum,
																 AgreementRebateOrderCashStatusEnum agreementRebateOrderCashStatusEnum);
}
