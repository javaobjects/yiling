package com.yiling.sales.assistant.app.order.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.form
 * @date: 2022/1/11
 */
@Data
@Accessors(chain = true)
public class QueryOrderDetailForm {

    @NotNull
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @NotNull
    @ApiModelProperty(value = "订单明细ID")
    private Long orderDetailId;
}
