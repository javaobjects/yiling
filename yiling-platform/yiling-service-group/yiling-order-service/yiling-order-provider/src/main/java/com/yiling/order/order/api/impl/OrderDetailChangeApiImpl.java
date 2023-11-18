package com.yiling.order.order.api.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.entity.OrderDetailChangeDO;
import com.yiling.order.order.service.OrderDetailChangeService;

/**
 * 订单明细变更信息 服务类
 * @author:wei.wang
 * @date:2021/8/4
 */
@DubboService
public class OrderDetailChangeApiImpl implements OrderDetailChangeApi {

    @Autowired
    private OrderDetailChangeService orderDetailChangeService;

    /**
     * 根据订单明细ID获取订单明细变更信息
     *
     * @param orderId 订单明细ID
     * @return
     */
    @Override
    public List<OrderDetailChangeDTO> listByOrderId(Long orderId) {
        return PojoUtils.map(orderDetailChangeService.listByOrderId(orderId),OrderDetailChangeDTO.class);
    }

    /**
     * 更新票折金额
     *
     * @param detailId             订单明细ID
     * @param ticketDiscountAmount 票折金额
     * @param afterReceiveFlag     是否在收货后
     * @return
     */
    @Override
    public boolean updateTicketDiscountAmountByDetailId(Long detailId, BigDecimal ticketDiscountAmount, boolean afterReceiveFlag) {
        return orderDetailChangeService.updateTicketDiscountAmountByDetailId(detailId,ticketDiscountAmount,afterReceiveFlag);

}
    /**
     * 根据订单明细ID获取订单明细变更信息
     *
     * @param detailId 订单明细ID
     * @return
     */
    @Override
    public OrderDetailChangeDTO getByDetailId(Long detailId) {
        OrderDetailChangeDO orderDetailChangeDO = orderDetailChangeService.getByDetailId(detailId);
        return PojoUtils.map(orderDetailChangeDO, OrderDetailChangeDTO.class);
    }

    /**
     * 根据订单IDS获取订单明细变更信息
     *
     * @param orderIds 订单集合
     * @return
     */
    @Override
    public List<OrderDetailChangeDTO> listByOrderIds(List<Long> orderIds) {
        return PojoUtils.map(orderDetailChangeService.listByOrderIds(orderIds),OrderDetailChangeDTO.class);
    }

}
