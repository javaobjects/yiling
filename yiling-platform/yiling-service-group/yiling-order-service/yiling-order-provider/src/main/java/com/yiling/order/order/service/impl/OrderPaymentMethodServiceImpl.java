package com.yiling.order.order.service.impl;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dto.request.CreateOrderPayMentMethodRequest;
import com.yiling.order.order.entity.OrderPaymentMethodDO;
import com.yiling.order.order.dao.OrderPaymentMethodMapper;
import com.yiling.order.order.service.OrderPaymentMethodService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单支付记录表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-10-09
 */
@Service
public class OrderPaymentMethodServiceImpl extends BaseServiceImpl<OrderPaymentMethodMapper, OrderPaymentMethodDO> implements OrderPaymentMethodService {

    @Override
    public Boolean save(CreateOrderPayMentMethodRequest payMentMethodRequest) {

        OrderPaymentMethodDO paymentMethodDO = PojoUtils.map(payMentMethodRequest,OrderPaymentMethodDO.class);

        return this.save(paymentMethodDO);

    }
}
