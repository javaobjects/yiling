package com.yiling.payment.web.receiptdesk.form;

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
public class OrderPayAmountForm extends BaseForm {
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

}
