package com.yiling.admin.hmc.insurance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退保详情
 * @author gxl
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceRecordRetreatVO extends BaseVO {

    /**
     * 参保记录表id
     */
    private Long insuranceRecordId;

    /**
     * 保单号
     */
    @ApiModelProperty(value = "对应保单单号")
    private String policyNo;
    /**
     * 退保单号
     */
    @ApiModelProperty(value = "退保单号")
    private String orderNo;



    /**
     * 退费金额
     */
    @ApiModelProperty(value = "退保金额")
    private BigDecimal retMoney;

    /**
     * 退保时间 yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty(value = "退保时间")
    private Date retTime;



    /**
     * 保单状态类型 15-是保单注销,16 - 是保单退保,18 - 是保险合同终止,71 - 保单失效
     */
    @ApiModelProperty(value = "保单状态类型 15-是保单注销,16 - 是保单退保,18 - 是保险合同终止,71 - 保单失效")
    private Integer endPolicyType;




    @ApiModelProperty(value = "投保人")
    private String holderName;

    @ApiModelProperty(value = "投保人电话")
    private String holderPhone;

    @ApiModelProperty(value = "投保人证件号")
    private String holderCredentialNo;
    /**
     * 保险名称
     */
    @ApiModelProperty(value = "保险名称")
    private String insuranceName;
    /**
     * 保单服务商
     */
    @ApiModelProperty(value = "保单服务商")
    private String companyName;
    /**
     * 第三方保险标识
     */
    @ApiModelProperty(value = "第三方保险标识")
    private String comboName;
}
