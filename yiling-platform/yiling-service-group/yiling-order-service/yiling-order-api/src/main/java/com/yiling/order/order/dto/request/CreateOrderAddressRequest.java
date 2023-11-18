package com.yiling.order.order.dto.request;

import lombok.Data;

/**
 * 创建订单收货地址 Request
 *
 * @author: xuan.zhou
 * @date: 2021/7/19
 */
@Data
public class CreateOrderAddressRequest implements java.io.Serializable {

    private static final long serialVersionUID = 6715603656270040446L;

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
