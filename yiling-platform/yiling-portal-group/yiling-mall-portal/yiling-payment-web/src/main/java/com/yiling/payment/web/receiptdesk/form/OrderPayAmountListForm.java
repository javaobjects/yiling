package com.yiling.payment.web.receiptdesk.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批量还款
 * @author:wei.wang
 * @date:2021/11/8
 */
@Data
public class OrderPayAmountListForm {

    @NotEmpty(message = "不能为空")
    @ApiModelProperty(value = "订单信息", required = true)
    List< @Valid  OrderPayAmountForm> list;

    @Min(1)
    @Max(4)
    @ApiModelProperty(value = "交易类型,1:定金 2:订单支付，3:还款, 4 尾款", required = true)
    private Integer tradeType;
}
