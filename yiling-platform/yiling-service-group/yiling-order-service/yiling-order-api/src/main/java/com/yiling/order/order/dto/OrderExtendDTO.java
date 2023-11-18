package com.yiling.order.order.dto;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单字段扩展表
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderExtendDTO extends BaseDO {
    private static final long serialVersionUID = 1L;

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 买家所属省份名称
     */
    private String buyerProvinceName;

    /**
     * 买家所属城市名称
     */
    private String buyerCityName;

    /**
     * 买家所属区域名称
     */
    private String buyerRegionName;

    /**
     * 买家所属省份编码
     */
    private String buyerProvinceCode;

    /**
     * 买家所属城市编码
     */
    private String buyerCityCode;

    /**
     * 买家所属区域编码
     */
    private String buyerRegionCode;

    /**
     * 卖家所属省份名称
     */
    private String sellerProvinceName;

    /**
     * 卖家所属城市名称
     */
    private String sellerCityName;

    /**
     * 卖家所属区域名称
     */
    private String sellerRegionName;

    /**
     * 卖家所属省份编码
     */
    private String sellerProvinceCode;

    /**
     * 卖家所属城市编码
     */
    private String sellerCityCode;

    /**
     * 卖家所属区域编码
     */
    private String sellerRegionCode;

    /**
     * 下单时是否会员 1-非会员 2-是会员
     */
    private Integer vipFlag;




}
