package com.yiling.f2b.admin.enterprise.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账期已还款订单金额总计
 * @author lun.yu
 * @date 2021年8月27日
 */
@ApiModel
@Data
@Accessors(chain = true)
public class PaymentRepaymentOrderVO {

    /**
     * 账户类型：1-以岭账期 2-非以岭账期 3-工业直属账期
     */
    @ApiModelProperty("账户类型：1-以岭账期 2-非以岭账期 3-工业直属账期")
    private Integer type;

    /**
     * 授信主体
     */
    @ApiModelProperty(value = "授信主体(供应商)")
    private String ename;

    /**
     * 客户名称（采购商）
     */
    @ApiModelProperty(value = "客户名称（采购商）")
    private String customerName;

    /**
     * 已还款金额
     */
    @ApiModelProperty("已还款金额")
    private BigDecimal repaymentAmount;

    /**
     * 历史已还款金额
     */
    @ApiModelProperty("历史已还款金额")
    private BigDecimal historyRepaymentAmount;

    /**
     * 总还款金额
     */
    @ApiModelProperty("总还款金额")
    private BigDecimal totalRepaymentAmount;

    /**
     * 待还款金额
     */
    @ApiModelProperty("待还款金额")
    private BigDecimal unRepaymentAmount;

    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    /**
     * 历史已使用额度
     */
    @ApiModelProperty(value = "历史已使用额度")
    private BigDecimal historyUsedAmount;

    /**
     * 总已使用额度
     */
    @ApiModelProperty(value = "已使用额度")
    private BigDecimal totalUsedAmount;

}
