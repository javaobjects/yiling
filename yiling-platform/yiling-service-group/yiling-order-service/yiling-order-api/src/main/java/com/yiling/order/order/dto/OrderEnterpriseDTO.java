package com.yiling.order.order.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业订单DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderEnterpriseDTO extends BaseDTO {

    /**
     *订单编号
     */
    private String orderNo;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 支付状态：1-待支付 2-部分支付,3-已支付
     */
    private Integer paymentStatus;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 买家企业名称
     */
    private String buyerEname;

    /**
     * 买家企业省份名称
     */
    private String buyerProvinceName;

    /**
     * 买家企业城市名称
     */
    private String buyerCityName;

    /**
     * 买家企业去名称
     */
    private String buyerRegionName;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;

    /**
     * 卖家企业名称
     */
    private String sellerEname;

    /**
     * 卖家企业省份名称
     */
    private String sellerProvinceName;

    /**
     * 卖家企业城市名称
     */
    private String sellerCityName;

    /**
     * 卖家企业区名称
     */
    private String sellerRegionName;
}
