package com.yiling.f2b.admin.order.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 申请开票信息
 * @author:wei.wang
 * @date:2021/12/29
 */
@Data
public class SaveOrderTicketBatchForm {

    /**
     * 批次编号
     */
    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "批次编号")
    private String batchNo;

    /**
     * 开票数量
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "开票数量")
    private Integer invoiceQuantity;
}
