package com.yiling.b2b.app.order.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Data
@Accessors(chain = true)
public class B2BOrderReturnDetailBatchApplyForm {

    @ApiModelProperty(value = "批次号", required = true)
    private String batchNo;
    
    @ApiModelProperty(value = "退货数量", required = true)
    private Integer returnQuantity;
}
