package com.yiling.hmc.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.hmc.order.api.OrderRelateUserApi;
import com.yiling.hmc.order.dto.OrderRelateUserDTO;
import com.yiling.hmc.order.enums.HmcOrderRelateUserTypeEnum;
import com.yiling.hmc.order.service.OrderRelateUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单关联用户
 *
 * @author: fan.shen
 * @date: 2022/4/27
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderRelateUserApiImpl implements OrderRelateUserApi {

    private final OrderRelateUserService orderRelateUserService;

    @Override
    public OrderRelateUserDTO queryByOrderIdAndRelateType(Long orderId, HmcOrderRelateUserTypeEnum userTypeEnum) {
        return orderRelateUserService.queryByOrderIdAndRelateType(orderId, userTypeEnum);
    }

}
