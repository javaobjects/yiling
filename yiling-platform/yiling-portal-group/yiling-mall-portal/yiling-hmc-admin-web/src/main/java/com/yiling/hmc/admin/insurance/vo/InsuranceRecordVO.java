package com.yiling.hmc.admin.insurance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * C端参保记录表
 * </p>
 *
 * @author gxl
 * @date 2022-03-28
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "payInsuranceRecordVO")
public class InsuranceRecordVO extends BaseVO {

    @ApiModelProperty(value = "保险id")
    private Long insuranceId;

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;

    private Integer userId;

    @ApiModelProperty(value = "保司保单号")
    private String policyNo;

    @ApiModelProperty(value = "平台单号")
    private String orderNo;
    @ApiModelProperty(value = "泰康支付订单号")
    private String billNo;

    @ApiModelProperty(value = "支付流水号")
    private String transactionId;

    @ApiModelProperty(value = "定额方案类型 1-季度，2-年")
    private Integer billType;

    @ApiModelProperty(value = "投保时间")
    private Date proposalTime;

    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "终止时间")
    private Date endTime;

    @ApiModelProperty(value = "保单生效时间")
    private Date effectiveTime;

    @ApiModelProperty(value = "是否退保")
    private Boolean isRetreat;

    @ApiModelProperty(value = "被保人名称")
    private String issueName;

    @ApiModelProperty(value = "被保人联系方式")
    private String issuePhone;

    @ApiModelProperty(value = "保险详情id")
    private Integer policyInfoId;

    @ApiModelProperty(value = "销售人员id 来源类型也通过此字段判断 0-线上渠道 大于0 线下终端")
    private Long sellerUserId;

    @ApiModelProperty(value = "销售员姓名")
    private String sellerUserName;

    @ApiModelProperty(value = "保单状态 1-进行中，2-已退保，3-已终止，4-已失效")
    private Integer policyStatus;


    @ApiModelProperty(value = "保司")
    private String companyName;

    @ApiModelProperty(value = "销售所属企业")
    private String terminalName;

    @ApiModelProperty(value = "被保人身份证")
    private String issueCredentialNo;

    @ApiModelProperty(value = "投保人身份证")
    private String holderCredentialNo;

    @ApiModelProperty("当前所交期")
    private Integer currentPayStage;

    @ApiModelProperty("保险兑付药店")
    private String orderSource;

    @ApiModelProperty("对应保司定额标识")
    private String comboName;

    @ApiModelProperty("保单支付额")
    private BigDecimal amount;

    @ApiModelProperty("投保人")
    private String holderName;

    @ApiModelProperty("投保人手机号")
    private String holderPhone;

    @ApiModelProperty("电子保单")
    private String policyUrl;
}
