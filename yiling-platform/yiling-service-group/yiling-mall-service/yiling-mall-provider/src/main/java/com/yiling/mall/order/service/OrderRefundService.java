package com.yiling.mall.order.service;

import com.yiling.mall.order.dto.request.RefundOrderRequest;
import com.yiling.mall.order.dto.request.RefundReTryRequest;

/**
 * @author: yong.zhang
 * @date: 2021/11/5
 */
public interface OrderRefundService {

    /**
     * 申请退款
     *
     * @param request
     * @return
     */
    boolean refundOrder(RefundOrderRequest request);

    /**
     * 退款失败财务处理
     *
     * @param request
     * @return
     */
    boolean reTryRefund(RefundReTryRequest request);
}
