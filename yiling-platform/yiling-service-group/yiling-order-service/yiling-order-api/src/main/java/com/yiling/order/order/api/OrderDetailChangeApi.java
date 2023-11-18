package com.yiling.order.order.api;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.order.order.dto.OrderDetailChangeDTO;

/**
 * 订单明细变更信息 服务类
 * @author:wei.wang
 * @date:2021/8/4
 */
public interface OrderDetailChangeApi {
    /**
     * 根据订单ID获取订单明细变更信息
     *
     * @param orderId 订单ID
     * @return
     */
    List<OrderDetailChangeDTO> listByOrderId(Long orderId);

    /**
     *根据订单IDS获取订单明细变更信息
     * @param orderIds 订单集合
     * @return
     */
    List<OrderDetailChangeDTO> listByOrderIds(List<Long> orderIds);

    /**
     * 更新票折金额
     *
     * @param detailId 订单明细ID
     * @param ticketDiscountAmount 票折金额
     * @param afterReceiveFlag 是否在收货后
     * @return
     */
    boolean updateTicketDiscountAmountByDetailId(Long detailId, BigDecimal ticketDiscountAmount, boolean afterReceiveFlag);


    /**
     * 根据订单明细ID获取订单明细变更信息
     *
     * @param detailId 订单明细ID
     * @return
     */
    OrderDetailChangeDTO getByDetailId(Long detailId);


}
