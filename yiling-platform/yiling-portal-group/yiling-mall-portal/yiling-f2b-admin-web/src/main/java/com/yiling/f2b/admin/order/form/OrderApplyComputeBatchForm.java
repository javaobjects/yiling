package com.yiling.f2b.admin.order.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 计算金额明细接口
 * @author:wei.wang
 * @date:2021/12/28
 */
@Data
public class OrderApplyComputeBatchForm {

    /**
     * 批次编号
     */
    @ApiModelProperty(value = "批次编号",required = true)
    @NotBlank
    private String batchNo;

    /**
     * 开票数量
     */
    @ApiModelProperty(value = "开票数量",required = true)
    @NotNull
    private Integer invoiceQuantity;
}
