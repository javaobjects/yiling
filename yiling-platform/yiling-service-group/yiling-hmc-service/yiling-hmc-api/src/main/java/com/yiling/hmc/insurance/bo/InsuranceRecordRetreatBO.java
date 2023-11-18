package com.yiling.hmc.insurance.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class InsuranceRecordRetreatBO extends BaseDTO {

    private static final long serialVersionUID = 6648983873473875934L;
    /**
     * 参保记录表id
     */
    private Long insuranceRecordId;

    /**
     * 保单号
     */
    private String policyNo;
    /**
     * 退保单号
     */
    private String orderNo;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 退费金额 单位：分
     */
    private BigDecimal retMoney;

    /**
     * 退保时间 yyyy-MM-dd HH:mm:ss
     */
    private Date retTime;



    /**
     * 保单状态类型 15-是保单注销,16 - 是保单退保,18 - 是保险合同终止,71 - 保单失效
     */
    private Integer endPolicyType;


    /**
     * 投保人
     */
    private Long  userId;


    private String holderName;

    private String holderPhone;

    private String holderCredentialNo;
    /**
     * 保险名称
     */
    private String insuranceName;
    /**
     * 保单服务商
     */
    private String companyName;
    /**
     * 第三方保险标识
     */
    private String comboName;
}
