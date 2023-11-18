package com.yiling.hmc.payment.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderPayAmountListForm {

    @NotEmpty(message = "不能为空")
    @ApiModelProperty(value = "订单信息", required = true)
    List< @Valid  OrderPayAmountForm> list;

    @Min(1)
    @Max(7)
    @NotNull
    @ApiModelProperty(value = "交易类型,2:订单支付 6:问诊 7:处方", required = true)
    private Integer tradeType;
}
