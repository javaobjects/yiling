package com.yiling.sales.assistant.app.invoice.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 选择票折信息
 * @author:wei.wang
 * @date:2021/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderChoiceTicketInfoVO extends BaseVO {
    /**
     * 票折单号
     */
    @ApiModelProperty(value = "票折单号")
    private String ticketDiscountNo;

    /**
     * 票折金额
     */
    @ApiModelProperty(value = "票折金额")
    private BigDecimal totalAmount;

    /**
     * 票折已使用金额
     */
    @ApiModelProperty(value = "票折已使用金额")
    private BigDecimal usedAmount;

    /**
     * 票折可使用金额
     */
    @ApiModelProperty(value = "票折可使用金额")
    private BigDecimal availableAmount;

    @ApiModelProperty(value = "使用票折订单数")
    private Integer usedOrder;

}
