package com.yiling.f2b.admin.order.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOrderInvoiceAmountErpDeliveryNoForm  {
    /**
     * 出库单号
     */
    @NotBlank
    @ApiModelProperty(value = "出库单号")
    private String erpDeliveryNo;

    /**
     *开票明细信息
     */
    @NotEmpty
    @ApiModelProperty(value = "开票明细信息")
    private List<@Valid QueryOrderInvoiceAmountDetailForm> detailFormList;
}
