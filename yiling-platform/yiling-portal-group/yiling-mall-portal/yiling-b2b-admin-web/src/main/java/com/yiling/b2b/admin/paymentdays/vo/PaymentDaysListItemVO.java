package com.yiling.b2b.admin.paymentdays.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 账期额度管理列表VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentDaysListItemVO extends BaseVO {

    /**
     * 账户类型：1-以岭账期 2-非以岭账期
     */
    @ApiModelProperty("账户类型：1-以岭账期 2-非以岭账期 3-工业直属账期")
    private Integer type;

    /**
     * 客户id（采购商）
     */
    @ApiModelProperty(value = "客户id（采购商）")
    private String customerEid;

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
     * 已使用额度 = 支付金额
     */
    @ApiModelProperty(value = "已使用额度")
    private BigDecimal usedAmount;

    /**
     * 已还款额度
     */
    @ApiModelProperty(value = "已还款额度")
    private BigDecimal repaymentAmount;

    /**
     * 待还款额度
     */
    @ApiModelProperty(value = "待还款额度")
    private BigDecimal needRepaymentAmount;

    /**
     * 可用额度
     */
    @ApiModelProperty(value = "可用额度")
    private BigDecimal availableAmount;

    /**
     * 还款周期
     */
    @ApiModelProperty(value = "还款周期")
    private Integer period;

    /**
     * 账期上浮点位（百分比）
     */
    @ApiModelProperty(value = "账期上浮点位（百分比）")
    private BigDecimal upPoint;

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
