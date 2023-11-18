package com.yiling.hmc.order.api;

import com.yiling.hmc.order.dto.OrderRelateUserDTO;
import com.yiling.hmc.order.enums.HmcOrderRelateUserTypeEnum;

/**
 * 订单管理用户API
 *
 * @author: fan.shen
 * @date: 2022/4/27
 */
public interface OrderRelateUserApi {

    /**
     * 通过订单id查询订单关联用户信息
     *
     * @param orderId 订单id
     * @param userTypeEnum 用户类型
     * @return 通过订单id查询订单关联用户信息
     */
    OrderRelateUserDTO queryByOrderIdAndRelateType(Long orderId, HmcOrderRelateUserTypeEnum userTypeEnum);
    
}
