package com.yiling.sales.assistant.app.order.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/01/06
 */
@Data
@Accessors(chain = true)
public class OrderReturnDetailBatchApplyForm {

    @ApiModelProperty(value = "批次号", required = true)
    private String  batchNo;

    @ApiModelProperty(value = "退货数量", required = true)
    private Integer returnQuantity;
}
