package com.yiling.settlement.b2b.api;

import com.yiling.settlement.b2b.dto.SettlementOrderSyncDTO;
import com.yiling.settlement.b2b.dto.request.UpdateOrderSyncSettStatusRequest;

/**
 * @author: dexi.yao
 * @date: 2022-04-11
 */
public interface SettlementOrderSyncApi {

    /**
     * 订单账期还款通知结算单业务
     *
     * @param orderCode
     * @return
     */
    Boolean cashRepaymentNotifySett(String orderCode);

    /**
     * 同步结算单同步失败的 订单数据
     */
    void settOrderSyncFailData();

    /**
     * 根据订单号查询订单数据
     *
     * @param orderCode
     * @return
     */
    SettlementOrderSyncDTO querySettOrderSyncByOrderCode(String orderCode);


}
