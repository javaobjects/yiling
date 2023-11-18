package com.yiling.b2b.admin.order.form;

import javax.validation.constraints.NotNull;

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
    @NotNull(message = "批次号不能为空")
    private String batchNo;
    
    @ApiModelProperty(value = "退货数量", required = true)
    @NotNull(message = "退货数量不能为空")
    private Integer returnQuantity;
}
