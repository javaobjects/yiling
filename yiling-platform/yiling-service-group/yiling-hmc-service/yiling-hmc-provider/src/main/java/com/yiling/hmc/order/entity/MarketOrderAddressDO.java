package com.yiling.hmc.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * HMC订单地址表
 * </p>
 *
 * @author fan.shen
 * @date 2023-02-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_market_order_address")
public class MarketOrderAddressDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
