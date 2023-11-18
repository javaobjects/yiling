package com.yiling.b2b.app.order.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批量支付订单参数
 * @author:wei.wang
 * @date:2021/11/8
 */
@Data
public class OrderB2BAppPayAmountForm extends BaseForm {
    /**
     * 订单id
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "订单id", required = true)
    private Long orderId;

    /**
     * 订单编号
     */
    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;

    /**
     * 应付金额
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "应付金额", required = true)
    private BigDecimal payAmount;

    /**
     * 采购商Id
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "采购商Id", required = true)
    private Long buyerEid;

    /**
     * 供应商id
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "供应商id", required = true)
    private Long sellerEid;
}
