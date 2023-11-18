package com.yiling.user.payment.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 账期已还款订单金额总计
 * @author lun.yu
 * @date 2021年8月27日
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PaymentRepaymentOrderDTO extends BaseDTO {

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
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 驳回退货单的退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 已还款金额
     */
    private BigDecimal repaymentAmount;
    /**
     * 历史已还款额度
     */
    private BigDecimal historyRepaymentAmount;

    /**
     * 总还款额度
     */
    private BigDecimal totalRepaymentAmount;

    /**
     * 待还款金额
     */
    private BigDecimal unRepaymentAmount;

    /**
     * 历史使用额度
     */
    private BigDecimal historyUsedAmount;

    /**
     * 总使用额度 = 历史使用额度 + 总订单金额
     */
    private BigDecimal totalUsedAmount;

}
