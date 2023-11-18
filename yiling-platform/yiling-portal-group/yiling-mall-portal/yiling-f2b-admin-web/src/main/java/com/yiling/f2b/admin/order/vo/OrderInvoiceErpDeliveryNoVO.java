package com.yiling.f2b.admin.order.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 出库单明细商品
 * @author:wei.wang
 * @date:2021/10/12
 */
@Data
public class OrderInvoiceErpDeliveryNoVO {
    /**
     * 出库单单号
     */
    @ApiModelProperty(value = "出库单单号")
    private String erpDeliveryNo;

    @ApiModelProperty(value = "商品信息")
    private List<OrderApplyInvoiceDetailVO> applyInvoiceDetailInfo;
}
