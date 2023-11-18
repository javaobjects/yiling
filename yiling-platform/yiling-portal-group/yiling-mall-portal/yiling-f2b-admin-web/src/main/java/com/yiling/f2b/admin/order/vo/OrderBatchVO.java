package com.yiling.f2b.admin.order.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批次信息
 * @author:wei.wang
 * @date:2021/10/11
 */
@Data
public class OrderBatchVO {

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNo;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private Date expiryDate;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    private Date produceDate;

    /**
     * 发货数量
     */
    @ApiModelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    /**
     * 开票数量
     */
    @ApiModelProperty(value = "剩余开票数量")
    private Integer remainInvoiceQuantity;

    /**
     * 开票数量
     */
    @ApiModelProperty(value = "开票数量")
    private Integer invoiceQuantity;

}
