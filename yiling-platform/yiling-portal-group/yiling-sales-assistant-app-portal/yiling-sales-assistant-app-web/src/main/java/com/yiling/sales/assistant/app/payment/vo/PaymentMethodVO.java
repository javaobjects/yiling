package com.yiling.sales.assistant.app.payment.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhigang.guo
 * @date: 2022/3/2
 */
@Data
@ApiModel("POP支付方式")
public class PaymentMethodVO  implements Serializable {

    @ApiModelProperty("支付方式ID")
    private Long id;

    @ApiModelProperty("支付方式名称")
    private String name;

    @ApiModelProperty("是否可用")
    private Boolean enabled;

    @ApiModelProperty("不可用原因")
    private String disabledReason;
}
