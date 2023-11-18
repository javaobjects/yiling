package com.yiling.f2b.admin.order.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 开票关联出库单信息
 * @author:wei.wang
 * @date:2021/10/8
 */
@Data
@Deprecated
public class OrderInvoiceGroupDeliveryForm implements java.io.Serializable{
    /**
     * 出库单集合
     */
    @ApiModelProperty(value = "出库单集合")
    @NotEmpty(message = "不能为空")
    private List<String> erpDeliveryNoList;

    /**
     * 开票摘要
     */
    @ApiModelProperty(value = "开票摘要")
    private String invoiceSummary;
}
