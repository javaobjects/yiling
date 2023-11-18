package com.yiling.user.agreement.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.request.AgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.CashAgreementRequest;
import com.yiling.user.agreement.dto.request.ClearAgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.PageListByIdRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailPageListRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderPageListRequest;
import com.yiling.user.agreement.entity.AgreementRebateOrderDetailDO;
import com.yiling.user.agreement.enums.AgreementRebateOrderCashStatusEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderConditionStatusEnum;

/**
 * <p>
 * 协议兑付订单明细表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-07-07
 */
public interface AgreementRebateOrderDetailService extends BaseService<AgreementRebateOrderDetailDO> {

	/**
	 * 计算协议政策
	 *
	 * @param request
	 * @return
	 */
	Boolean updateBatchDiscountAmount(AgreementConditionCalculateRequest request);

	/**
	 * 重新计算协议
	 *
	 * @param request
	 * @return
	 */
	Boolean clearDiscountAmountByOrderIdsAndAgreementIds(ClearAgreementConditionCalculateRequest request);

	/**
	 * 兑付协议
	 *
	 * @param request
	 * @return
	 */
	Boolean cashAgreementRebateOrderDetail(CashAgreementRequest request);

	/**
	 * 根据订单id查询返利订单明细
	 *
	 * @param request
	 * @return
	 */
	List<AgreementRebateOrderDetailDTO> queryAgreementRebateOrderDetailList(QueryRebateOrderDetailRequest request);

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

	/**
	 * 根据主键id分页查询订单明细
	 *
	 * @param request
	 * @return
	 */
	Page<AgreementRebateOrderDetailDTO> pageListById(PageListByIdRequest request);

	/**
	 * 根据订单id和退款单id分页查询返利订单明细
	 *
	 * @param request
	 * @return
	 */
	Page<AgreementRebateOrderDetailDTO> queryAgreementRebateOrderDetailPageList(QueryRebateOrderDetailPageListRequest request);
}
