package com.yiling.mall.payment.api;

/**
 * 账期订单 API
 *
 * @author lun.yu
 * @date 2021/8/10
 */
public interface PaymentDaysOrderApi {

    /**
     * 更新账期订单的还款金额
     * @param orderId 订单ID
     * @return
     */
    Boolean updatePaymentOrderAmount(Long orderId);
}
