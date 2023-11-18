package com.yiling.user.agreementv2.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议结算方式 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementSettlementMethodBO extends BaseVO implements Serializable {

    /**
     * 是否为预付款结算：0-否 1-是
     */
    @ApiModelProperty("是否为预付款结算")
    private Boolean advancePaymentFlag;

    /**
     * 是否为账期结算：0-否 1-是
     */
    @ApiModelProperty("是否为账期结算")
    private Boolean paymentDaysFlag;

    /**
     * 账期结算周期
     */
    @ApiModelProperty("账期结算周期")
    private Integer paymentDaysSettlementPeriod;

    /**
     * 账期结算日
     */
    @ApiModelProperty("账期结算日")
    private Integer paymentDaysSettlementDay;

    /**
     * 账期支付日
     */
    @ApiModelProperty("账期支付日")
    private Integer paymentDaysPayDays;

    /**
     * 是否为实销实结：0-否 1-是
     */
    @ApiModelProperty("是否为实销实结")
    private Boolean actualSalesSettlementFlag;

    /**
     * 实销实结结算周期
     */
    @ApiModelProperty("实销实结结算周期")
    private Integer actualSalesSettlementPeriod;

    /**
     * 实销实结结算日
     */
    @ApiModelProperty("实销实结结算日")
    private Integer actualSalesSettlementDay;

    /**
     * 实销实结支付日
     */
    @ApiModelProperty("实销实结支付日")
    private Integer actualSalesSettlementPayDays;

    /**
     * 是否为货到付款结算：0-否 1-是
     */
    @ApiModelProperty("是否为货到付款结算")
    private Boolean payDeliveryFlag;

    /**
     * 是否为压批结算：0-否 1-是
     */
    @ApiModelProperty("是否为压批结算")
    private Boolean pressGroupFlag;

    /**
     * 压批次数
     */
    @ApiModelProperty("压批次数")
    private Integer pressGroupNumber;

    /**
     * 是否为授信结算：0-否 1-是
     */
    @ApiModelProperty("是否为授信结算")
    private Boolean creditFlag;

    /**
     * 授信金额
     */
    @ApiModelProperty("授信金额")
    private Integer creditAmount;

    /**
     * 到货周期
     */
    @Range(min = 1, max = 999)
    @ApiModelProperty("到货周期")
    private Integer arrivePeriod;

    /**
     * 最小发货量
     */
    @Range(min = 1, max = 9999)
    @ApiModelProperty("最小发货量")
    private Integer minShipmentNumber;

    /**
     * 送货方式：1-送货上门 2-公司配送 3-客户自提 4-委托配送
     */
    @ApiModelProperty("送货方式：1-送货上门 2-公司配送 3-客户自提 4-委托配送（见字典：agreement_delivery_type）")
    private Integer deliveryType;

    /**
     * 采购负责人
     */
    @ApiModelProperty("采购负责人")
    private Long buyerId;

    /**
     * 采购负责人名称
     */
    @ApiModelProperty("采购负责人名称")
    private String buyerName;

}
