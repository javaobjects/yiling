package com.yiling.mall.payment.service;

/**
 * 账期订单 Service
 *
 * @author: lun.yu
 * @date: 2021/8/10
 */
public interface PaymentDaysOrderService {

    /**
     * 更新账期订单的还款金额
     *
     * @param orderId 订单ID
     * @return
     */
    Boolean updatePaymentOrderAmount(Long orderId);
}
