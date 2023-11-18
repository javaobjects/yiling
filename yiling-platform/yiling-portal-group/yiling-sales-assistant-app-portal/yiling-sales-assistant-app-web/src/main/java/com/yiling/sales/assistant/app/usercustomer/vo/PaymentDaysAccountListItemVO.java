package com.yiling.sales.assistant.app.usercustomer.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户账期账户表
 * </p>
 *
 * @author gxl
 * @date 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentDaysAccountListItemVO extends BaseVO {

    /**
     * 账户类型：1-以岭账期 2-非以岭账期
     */
    @ApiModelProperty("账户类型：1-以岭账期 2-非以岭账期")
    private Integer type;

    /**
     * 企业id（供应商）
     */
    @ApiModelProperty(value = "企业id（供应商）")
    private Long eid;

    /**
     * 授信主体
     */
    @ApiModelProperty(value = "授信主体（供应商）")
    private String ename;

    /**
     * 客户id（采购商）
     */
    @ApiModelProperty(value = "客户id（采购商）")
    private Long customerEid;

    /**
     * 客户名称（采购商）
     */
    @ApiModelProperty(value = "客户名称（采购商）")
    private String customerName;

    /**
     * 账期额度
     */
    @ApiModelProperty(value = "账期额度")
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
     * 待还款额度
     */
    @ApiModelProperty(value = "待还款额度")
    private BigDecimal needRepaymentAmount;

    /**
     * 退款总金额
     */
    @ApiModelProperty(value = "退款总金额")
    private BigDecimal totalRefundAmount;

    /**
     * 支付总金额
     */
    @ApiModelProperty(value = "支付总金额")
    private BigDecimal totalPayAmount;

    /**
     * 支付总金额
     */
    @ApiModelProperty(value = "已使用总金额")
    private BigDecimal totalUsedAmount;

    /**
     * 还款周期
     */
    @ApiModelProperty(value = "还款周期")
    private Integer period;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

}
