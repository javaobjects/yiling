package com.yiling.mall.settlement.b2b.service;

import java.util.List;

/**
 * <p>
 * b2b商家结算单表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-19
 */
public interface SettlementService {


	/**
	 * 根据货款&预售违约结算单id更新结算单状态为已发起结算
	 *
	 * @param settlementIdList
	 * @param opUserId
	 * @return
	 */
	Boolean submitPayment(List<Long> settlementIdList, Long opUserId);

}
