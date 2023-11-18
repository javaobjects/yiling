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
 * 创建订单 Request
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderSubmitRequest extends BaseRequest {

    /**
     * 参保记录ID
     */
    @NotNull
    private Long insuranceRecordId;

    /**
     * C端用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 订单状态
     */
    private HmcOrderStatusEnum hmcOrderStatus = HmcOrderStatusEnum.UN_PAY;

    /**
     * 开方状态
     */
    private HmcPrescriptionStatusEnum prescriptionStatus = HmcPrescriptionStatusEnum.NOT_PRESCRIPTION;

    /**
     * 支付状态
     */
    private HmcPaymentStatusEnum paymentStatusEnum = HmcPaymentStatusEnum.UN_PAY;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 支付方式
     */
    private HmcPaymentMethodEnum paymentMethodEnum = HmcPaymentMethodEnum.INSURANCE_PAY;

    /**
     * 订单类型
     */
    private HmcOrderTypeEnum orderType = HmcOrderTypeEnum.MEDICINE;

    /**
     * 配送方式
     */
    private HmcDeliveryTypeEnum deliveryType = HmcDeliveryTypeEnum.SELF_PICKUP;

    /**
     * 订单创建来源
     */
    private HmcCreateSourceEnum createSource = HmcCreateSourceEnum.ADMIN_HMC;

    /**
     * 开方日期
     */
    private Date receiptDate;

    /**
     * 医生名称
     */
    private String doctor;

    /**
     * 诊断结果
     */
    private String interrogationResult;

    /**
     * 处方快照url
     */
    private String prescriptionSnapshotUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 订单票据，逗号分隔
     */
    private List<String> orderReceipts;

}
