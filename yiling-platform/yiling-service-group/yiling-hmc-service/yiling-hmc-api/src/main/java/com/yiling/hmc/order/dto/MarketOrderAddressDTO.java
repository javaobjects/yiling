package com.yiling.hmc.order.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class MarketOrderAddressDTO extends BaseDTO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 手机号座机
     */
    private String mobile;

    /**
     * 收货地址ID
     */
    private Long addressId;

    /**
     * 详细地址
     */
    private String address;

}
