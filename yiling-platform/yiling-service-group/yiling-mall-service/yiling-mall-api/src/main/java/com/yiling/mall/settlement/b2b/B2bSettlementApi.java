package com.yiling.mall.settlement.b2b;

import java.util.List;

/**
 * @author dexi.yao
 * @date 2021-10-19
 */
public interface B2bSettlementApi {


	/**
	 * 根据货款&预售违约结算单id更新结算单状态为已发起结算并发起打款
	 *
	 * @param settlementIdList
	 * @param opUserId
	 * @return
	 */
	Boolean submitPayment(List<Long> settlementIdList, Long opUserId);
}
