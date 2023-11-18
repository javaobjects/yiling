package com.yiling.f2b.admin.order.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 接受回执单信息
 * @author:wei.wang
 * @date:2021/8/3
 */
@Data
public class ReceiveReceiptForm implements java.io.Serializable {

    @ApiModelProperty(value = "订单ID",required = true)
    @NotNull(message = "不能为空")
    private  Long orderId;


    @ApiModelProperty(value = "回执单号",required = true)
    @NotEmpty(message = "不能为空")
    private List<String> receiveReceiptList;
}
