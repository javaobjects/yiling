package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderDeliveryReceivableDTO;

/**
 * 订单出库单和应收单关联关系api
 * @author:wei.wang
 * @date:2021/9/23
 */
public interface OrderDeliveryReceivableApi {
    /**
     * 根据订单id查询所有
     * @param orderIds
     * @return
     */
    List<OrderDeliveryReceivableDTO> listByOrderIds(List<Long> orderIds);

    /**
     * 根据应收单号获取出库单号
     * @param erpReceivableNo 应收单号
     * @return
     */
    String getDeliveryNo(String erpReceivableNo);
}
