package com.yiling.f2b.admin.enterprise.form;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
*
*@author:tingwei.chen
*@date:2021/6/28
*/

@Data
@Accessors(chain = true)
public class OrderDeliveryForm {

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号", required = true)
    private String batchNo;
    /**
     * 退货数量
     */
    @ApiModelProperty(value = "退货数量", required = true)
    private Integer returnQuantity;
}
