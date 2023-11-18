package com.yiling.settlement.b2b.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.b2b.dto.SettlementOrderDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementOrderPageListRequest;
import com.yiling.settlement.b2b.dto.request.UpdateSettlementStatusRequest;
import com.yiling.settlement.b2b.enums.SettlementOperationTypeEnum;

/**
 * @author dexi.yao
 * @date 2021-10-26
 */
public interface SettlementOrderApi {

	/**
	 * 分页查询结算单-订单对账单
	 *
	 * @param request
	 * @return
	 */
	Page<SettlementOrderDTO> querySettlementOrderPageList(QuerySettlementOrderPageListRequest request);

	/**
	 * 批量更新订单的结算单状态
	 *
	 * @param successList
	 * @param failList
	 * @param type    1:发起打款时调用 2：打款回调时调用
	 * @return
	 */
	Boolean updateSettlementStatus(List<UpdateSettlementStatusRequest> successList
			, List<UpdateSettlementStatusRequest> failList, SettlementOperationTypeEnum type);

    /**
     * 根据订单id查询结算订单记录
     *
     * @param orderId
     * @return
     */
    SettlementOrderDTO querySettOrderByOrderId(Long orderId);
}
