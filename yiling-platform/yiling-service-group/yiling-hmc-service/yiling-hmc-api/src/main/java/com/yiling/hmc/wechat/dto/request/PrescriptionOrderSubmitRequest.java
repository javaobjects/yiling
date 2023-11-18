package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.hmc.order.enums.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 创建处方订单 Request
 *
 * @author: fan.shen
 * @date: 2023/05/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PrescriptionOrderSubmitRequest extends BaseRequest {

    /**
     * 药店id
     */
    private Long eid;

    /**
     * 药店名称
     */
    private String ename;
    /**
     * IH 药店id
     */
    private Long ihEid;

    /**
     * IH 药店名称
     */
    private String ihEname;

    /**
     * IH配送商来源 1：以岭互联网医院IH 2：健康中心HMC
     */
    private Integer ihPharmacySource;

    /**
     * 处方类型 1：西药 0：中药
     */
    private Integer prescriptionType;

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

    /**
     * 处方id
     */
    private Integer prescriptionId;

    /**
     * 处方价格
     */
    private BigDecimal prescriptionPrice;

}
