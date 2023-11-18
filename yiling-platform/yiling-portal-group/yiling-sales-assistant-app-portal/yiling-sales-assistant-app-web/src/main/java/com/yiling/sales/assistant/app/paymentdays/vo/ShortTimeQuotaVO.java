package com.yiling.sales.assistant.app.paymentdays.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 账期临时额度列表专用dto
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ShortTimeQuotaVO extends BaseDTO {

    private static final long serialVersionUID = -1874940983299963004L;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商(授信主体)")
    private String ename;

    /**
     * 客户名称（采购商）
     */
    @ApiModelProperty(value = "客户名称（采购商）")
    private String customerName;

    /**
     * 账期额度
     */
    @ApiModelProperty(value = "账期额度(企业额度上限)")
    private BigDecimal totalAmount;

    /**
     * 临时额度
     */
    @ApiModelProperty(value = "临时额度")
    private BigDecimal temporaryAmount;

    /**
     * 已使用额度
     */
    @ApiModelProperty(value = "已使用额度")
    private BigDecimal usedAmount;

    /**
     * 已还款额度
     */
    @ApiModelProperty(value = "已还款额度")
    private BigDecimal repaymentAmount;

    /**
     * 可用额度
     */
    @ApiModelProperty(value = "可用额度")
    private BigDecimal availableAmount;

    /**
     * 申请临时额度
     */
    @ApiModelProperty(value = "申请临时额度")
    private BigDecimal shortTimeQuota;
    /**
     * 同意后可使用额度
     */
    @ApiModelProperty(value = "同意后可使用额度")
    private BigDecimal afterAgreementTotalAmount;

    /**
     * 未还款额度
     */
    @ApiModelProperty(value = "未还款额度")
    private BigDecimal unRepaymentAmount;

    /**
     * 其中到期未还款额度
     */
    @ApiModelProperty(value = "其中到期未还款额度")
    private BigDecimal exprUnRepaymentAmount;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "授信状态：1-启用 2-停用")
    private Integer status;

    /**
     * 还款周期（天）
     */
    @ApiModelProperty(value = "还款周期（天）")
    private Integer period;

    /**
     * 申请状态
     */
    @ApiModelProperty(value = "审核状态：1-待审核 2-审核通过 3-审核驳回)")
    private Long auditStatus;

    @ApiModelProperty(value = "申请时间")
    private Date createTime;

    @ApiModelProperty(value = "审核时间")
    private Date updateTime;

    @ApiModelProperty(value = "审核人")
    private String updateUser;

}
