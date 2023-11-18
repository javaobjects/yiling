package com.yiling.order.order.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderAddressDTO extends BaseDTO {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人电话
     */
    private String mobile;

    /**
     * 收货地址ID
     */
    private Long addressId;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;
}
