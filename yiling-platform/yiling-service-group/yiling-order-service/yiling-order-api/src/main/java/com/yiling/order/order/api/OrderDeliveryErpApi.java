package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderDeliveryErpDTO;

/**
 * 订单ERP出库单信息Api
 * @author:wei.wang
 * @date:2021/9/23
 */
public interface OrderDeliveryErpApi {

    /**
     * 根据订单id查询所有
     * @param orderIds
     * @return
     */
    List<OrderDeliveryErpDTO> listByOrderIds(List<Long> orderIds);

    /**
     * 根据订单查询
     * @param detailId
     * @param erpDeliveryNo
     * @return
     */
    @Deprecated
    List<OrderDeliveryErpDTO> listByDetailIdAndNo(Long detailId,String erpDeliveryNo);
}
