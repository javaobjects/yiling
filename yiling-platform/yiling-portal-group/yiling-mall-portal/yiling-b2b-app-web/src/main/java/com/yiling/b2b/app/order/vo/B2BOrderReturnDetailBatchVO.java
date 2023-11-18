package com.yiling.b2b.app.order.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Data
@Accessors(chain = true)
public class B2BOrderReturnDetailBatchVO {
    
    @ApiModelProperty(value = "退货单ID", required = true)
    private Long returnId;

    @ApiModelProperty(value = "订单明细ID", required = true)
    private Long detailId;

    @ApiModelProperty(value = "批次号", required = true)
    private String batchNo;

    @ApiModelProperty(value = "有效期", required = true)
    private Date expiryDate;

    @ApiModelProperty(value = "退货数量", required = true)
    private Integer returnQuantity;

}
