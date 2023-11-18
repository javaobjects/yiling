package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.hmc.order.enums.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 创建市场订单 Request
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MarketOrderSubmitRequest extends BaseRequest {

    /**
     * 药店id
     */
    private Long eid;

    /**
     * 药店名称
     */
    private Long ename;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 医生id
     */
    private Long doctorId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品数量
     */
    private Integer goodsQuantity;

    /**
     * 订单创建来源
     */
    private HmcCreateSourceEnum createSource;

    /**
     * 支付状态
     */
    private HmcPaymentStatusEnum paymentStatusEnum;

    /**
     * 订单状态
     */
    private HmcOrderStatusEnum hmcOrderStatus = HmcOrderStatusEnum.UN_PAY;

    /**
     * 支付方式
     */
    private HmcPaymentMethodEnum paymentMethodEnum;

    /**
     * 配送方式
     */
    private HmcDeliveryTypeEnum deliveryType;

    /**
     * 地址id
     */
    private Long addressId;

    /**
     * 备注
     */
    private String remark;

}
