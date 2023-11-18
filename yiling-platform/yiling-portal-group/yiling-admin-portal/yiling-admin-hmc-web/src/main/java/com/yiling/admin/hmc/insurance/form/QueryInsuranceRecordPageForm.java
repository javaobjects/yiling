package com.yiling.admin.hmc.insurance.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询我的参保
 *
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryInsuranceRecordPageForm extends QueryPageListForm {




    @ApiModelProperty(value = "保险服务商ID")
   private Long insuranceCompanyId;

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;
    /**
     * 投保时间
     */
    @ApiModelProperty(value = "投保时间-开始")
    private Date startProposalTime;
    @ApiModelProperty(value = "投保时间-结束")
    private Date endProposalTime;

    /**
     * 药店
     */
    @ApiModelProperty(value = "保单来源终端")
    private String terminalName;

    /**
     * 保司单号
     */
    @ApiModelProperty(value = "保司单号")
    private String policyNo;

    /**
     * 被报人姓名
     */
    @ApiModelProperty(value = "被报人姓名")
    private String issueName;

    @ApiModelProperty(value = "投/被保人手机号")
    private String issuePhone;

    @ApiModelProperty(value = "投保人姓名")
    private String holderName;

    @ApiModelProperty(value = "销售员手机号")
    private String sellerMobile;

    @ApiModelProperty(value = "销售员姓名")
    private String sellerName;

    /**
     * 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
     */
    @ApiModelProperty(value = "保单状态 1-进行中，2-已退保，3-已终止，4-已失效")
    private Integer policyStatus;

    /**
     * 定额方案类型 1-季度，2-年
     */
    @ApiModelProperty(value = "定额方案类型 1-季度，2-年")
    private Integer billType;

    /**
     * 0 线上 1 线下
     */
    @ApiModelProperty(value = "0 线上 1 线下")
    private Integer sourceType;
}
