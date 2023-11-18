package com.yiling.sales.assistant.app.payment.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhigang.guo
 * @date: 2022/3/1
 */
@Data
@ApiModel("POP支付类型")
public class PaymentTypeVO  implements Serializable{

    @ApiModelProperty("支付类型ID")
    private Integer id;

    @ApiModelProperty("支付方式列表")
    private List<PaymentMethodVO> paymentMethodList;
}
