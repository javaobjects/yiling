package com.yiling.settlement.b2b.service;

import com.yiling.settlement.b2b.dto.SettlementOrderCancelDTO;
import com.yiling.settlement.b2b.dto.SettlementOrderSyncDTO;
import com.yiling.settlement.b2b.entity.SettlementOrderCancelDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 结算单的预付款订单违约订单同步表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-10-11
 */
public interface SettlementOrderCancelService extends BaseService<SettlementOrderCancelDO> {

    /**
     * 根据订单号生成结算单所需要的尾款支付超时的订单数据
     *
     * @param orderCode
     * @return
     */
    Boolean createSettOrderCancelSync(String orderCode);

    /**
     * 根据订单号查询订单数据
     *
     * @param orderCode
     * @return
     */
    SettlementOrderCancelDTO querySettOrderSyncByOrderCode(String orderCode);
}
