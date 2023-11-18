package com.yiling.settlement.b2b.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.b2b.dto.SettlementDetailDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementDetailPageListRequest;

/**
 * @author dexi.yao
 * @date 2021-10-25
 */
public interface SettlementDetailApi {

	/**
	 * 分页查询结算单明细列表
	 *
	 * @param request
	 * @return
	 */
	Page<SettlementDetailDTO> querySettlementDetailPageList(QuerySettlementDetailPageListRequest request);

	/**
	 * 根据订单id查询结算单明细
	 *
	 * @param orderId
	 * @return
	 */
	List<SettlementDetailDTO> querySettlementDetailByOrderId(Long orderId);

	/**
	 * 根据结算单id查询结算单明细
	 *
	 * @param settlementIdList
	 * @return
	 */
	List<SettlementDetailDTO> querySettlementDetailBySettlementId(List<Long> settlementIdList);
}
