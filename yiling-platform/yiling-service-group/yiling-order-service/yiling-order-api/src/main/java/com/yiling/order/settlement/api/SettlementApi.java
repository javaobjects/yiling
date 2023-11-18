package com.yiling.order.settlement.api;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.settlement.dto.SettlementFullDTO;
import com.yiling.order.settlement.dto.request.SaveSettlementRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/8/10
 */
public interface SettlementApi {

    /**
     * 保存结算信息
     * @param request
     * @return
     */
    Boolean saveSettlement(SaveSettlementRequest request);

    /**
     * 查询需要计算的结算单信息
     * @param request
     * @return
     */
    List<SettlementFullDTO> getSettlementDetailByEidAndTime(QueryOrderUseAgreementRequest request);

    BigDecimal getsettlementAmountByOrderId(Long orderId);
}
