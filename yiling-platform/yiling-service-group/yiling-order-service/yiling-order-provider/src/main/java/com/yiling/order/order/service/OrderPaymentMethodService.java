package com.yiling.order.order.service;

import com.yiling.order.order.dto.request.CreateOrderPayMentMethodRequest;
import com.yiling.order.order.entity.OrderPaymentMethodDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 订单支付记录表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-10-09
 */
public interface OrderPaymentMethodService extends BaseService<OrderPaymentMethodDO> {

    /**
     * 保存订单支付方式
     * @param payMentMethodRequest
     */
    Boolean save(CreateOrderPayMentMethodRequest payMentMethodRequest);

}
