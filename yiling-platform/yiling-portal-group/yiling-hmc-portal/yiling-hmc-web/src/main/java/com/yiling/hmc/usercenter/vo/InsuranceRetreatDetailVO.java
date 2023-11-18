package com.yiling.hmc.usercenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 保单退保详情 VO
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@ApiModel
@Accessors(chain = true)
public class InsuranceRetreatDetailVO {

    private static final long serialVersionUID = 1L;

    /**
     * 参保记录表id
     */
    @ApiModelProperty("参保记录表id")
    private Long insuranceRecordId;

    /**
     * 保单号
     */
    @ApiModelProperty("保单号")
    private String policyNo;

    /**
     * 01 - 身份证
     */
    @ApiModelProperty("01 - 身份证")
    private String idType;

    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    private String idNo;

    /**
     * 被保人名称
     */
    @ApiModelProperty("被保人名称")
    private String issueName;

    /**
     * 被保人联系方式
     */
    @ApiModelProperty("被保人联系方式")
    private String issuePhone;

    /**
     * 退费金额 单位：分
     */
    @ApiModelProperty(value = "退费金额 单位：分", hidden = true)
    private Integer retMoney;

    /**
     * 退费金额 单位：元
     */
    @ApiModelProperty("退费金额 单位：元")
    private String retMoneyYuan;

    /**
     * 退保时间 yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty("退保时间")
    private Date retTime;

    /**
     * 保单状态类型 15-是保单注销,16 - 是保单退保,18 - 是保险合同终止,71 - 保单失效
     */
    @ApiModelProperty("保单状态类型 15-是保单注销,16 - 是保单退保,18 - 是保险合同终止,71 - 保单失效")
    private Integer endPolicyType;


}
