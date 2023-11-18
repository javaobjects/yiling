package com.yiling.user.agreementv2.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议结算方式 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementSettlementMethodRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 是否为预付款结算：0-否 1-是
     */
    private Boolean advancePaymentFlag;

    /**
     * 是否为账期结算：0-否 1-是
     */
    private Boolean paymentDaysFlag;

    /**
     * 账期结算周期
     */
    private Integer paymentDaysSettlementPeriod;

    /**
     * 账期结算日
     */
    private Integer paymentDaysSettlementDay;

    /**
     * 账期支付日
     */
    private Integer paymentDaysPayDays;

    /**
     * 是否为实销实结：0-否 1-是
     */
    private Boolean actualSalesSettlementFlag;

    /**
     * 实销实结结算周期
     */
    private Integer actualSalesSettlementPeriod;

    /**
     * 实销实结结算日
     */
    private Integer actualSalesSettlementDay;

    /**
     * 实销实结支付日
     */
    private Integer actualSalesSettlementPayDays;

    /**
     * 是否为货到付款结算：0-否 1-是
     */
    private Boolean payDeliveryFlag;

    /**
     * 是否为压批结算：0-否 1-是
     */
    private Boolean pressGroupFlag;

    /**
     * 压批次数
     */
    private Integer pressGroupNumber;

    /**
     * 是否为授信结算：0-否 1-是
     */
    private Boolean creditFlag;

    /**
     * 授信金额
     */
    private Integer creditAmount;

    /**
     * 到货周期
     */
    private Integer arrivePeriod;

    /**
     * 最小发货量
     */
    private Integer minShipmentNumber;

    /**
     * 送货方式：1-送货上门 2-公司配送 3-客户自提 4-委托配送
     */
    private Integer deliveryType;

    /**
     * 采购负责人
     */
    private Long buyerId;

}
