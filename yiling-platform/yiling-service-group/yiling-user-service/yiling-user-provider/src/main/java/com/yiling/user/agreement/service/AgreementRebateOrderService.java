package com.yiling.user.agreement.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.dto.AgreementOrderStatisticalDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDTO;
import com.yiling.user.agreement.dto.RebateOrderPageListDTO;
import com.yiling.user.agreement.dto.request.AgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.AgreementRebateOrderRequest;
import com.yiling.user.agreement.dto.request.CashAgreementRequest;
import com.yiling.user.agreement.dto.request.ClearAgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementRebateRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderPageListRequest;
import com.yiling.user.agreement.entity.AgreementRebateOrderDO;
import com.yiling.user.agreement.enums.AgreementRebateOrderCashStatusEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderConditionStatusEnum;

/**
 * <p>
 * 协议兑付订单表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-07-07
 */
public interface AgreementRebateOrderService extends BaseService<AgreementRebateOrderDO> {

    /**
     * 批量插入协议订单信息
     * @param agreementOrderList
     * @return
     */
    Boolean saveBatch(List<AgreementRebateOrderRequest> agreementOrderList);

    /**
     * 批量查询协议订单信息
     * @param request
     * @return
     */
    List<AgreementRebateOrderDTO> getAgreementRebateOrderListByAgreementIds(QueryAgreementRebateRequest request);

    /** agreement_account 删除
     * 计算协议政策
     * @param request
     * @return
     */
    Boolean updateBatchDiscountAmount(AgreementConditionCalculateRequest request);

    /**
     * 重新计算协议
     * @param request
     * @return
     */
    Boolean clearDiscountAmountByOrderIdsAndAgreementIds(ClearAgreementConditionCalculateRequest request);

    /**agreement_account 删除
     * 兑付
     * @param request
     * @return
     */
     Boolean cashAgreementByAgreementId(CashAgreementRequest request);


	/**
	 * 根据协议id统计可对账不可对账订单数量
	 *
	 * @param agreementId
	 * @return
	 */
	AgreementOrderStatisticalDTO statisticsOrderCount(Long agreementId);


	/**
	 * 根据条件枚举分页查询返利订单
	 *
	 * @param request
	 * @param conditionStatusEnum
	 * @return
	 */
	Page<RebateOrderPageListDTO> queryRebateOrderPageList(QueryRebateOrderPageListRequest request,
														  AgreementRebateOrderConditionStatusEnum conditionStatusEnum,
														  AgreementRebateOrderCashStatusEnum agreementRebateOrderCashStatusEnum);
}
