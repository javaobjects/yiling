package com.yiling.f2b.admin.order.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 出库关联开票
 * @author:wei.wang
 * @date:2021/10/11
 */
@Data
public class OrderInvoiceGroupVO {
    /**
     * 出库单明细商品
     */
    @ApiModelProperty(value = "出库单明细商品")
    private List<OrderInvoiceErpDeliveryNoVO> orderInvoiceErpDeliveryNoList;

    /**
     * 关联的出库单号
     */
    @ApiModelProperty(value = "关联的出库单号")
    private String groupDeliveryNos;

    /**
     *开票摘要
     */
    @ApiModelProperty(value = "开票摘要")
    private String invoiceSummary;
}
