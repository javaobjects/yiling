package com.yiling.f2b.web.order.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhigang.guo
 * @date: 2022/11/7
 */
@Data
public class CalculateDiscountForm {

    @ApiModelProperty(value = "配送商企业ID", required = true)
    private Long distributorEid;

    @ApiModelProperty(value = "卖家支付方式", required = true)
    private Integer paymentMethod;
}
