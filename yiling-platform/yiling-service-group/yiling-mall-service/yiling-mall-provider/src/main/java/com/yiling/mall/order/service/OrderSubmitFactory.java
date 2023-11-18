package com.yiling.mall.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.service
 * @date: 2021/10/27
 */
@Slf4j
@Component
public class OrderSubmitFactory {

    @Autowired
    private List<OrderSubmitService> orderSubmitServices;

    /**
     * 获取订单提交service
     *
     * @param orderTypeEnum
     * @param orderSourceEnum
     * @return
     */
    public OrderSubmitService getInstance(OrderTypeEnum orderTypeEnum, OrderSourceEnum orderSourceEnum) {

        return orderSubmitServices.stream().
                filter(e -> e.matchOrder(orderTypeEnum, orderSourceEnum)).findFirst().orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR));
    }

}
