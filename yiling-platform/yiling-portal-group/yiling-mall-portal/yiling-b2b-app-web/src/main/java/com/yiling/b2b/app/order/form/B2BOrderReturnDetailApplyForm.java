package com.yiling.b2b.app.order.form;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Data
@Accessors(chain = true)
public class B2BOrderReturnDetailApplyForm {

    @ApiModelProperty(value = "订单明细Id", required = true)
    private Long detailId;

    @ApiModelProperty(value = "商品id", required = true)
    private Long goodsId;

    @ApiModelProperty(value = "商品skuID", required = true)
    private Long goodsSkuId;

    @ApiModelProperty(value = "申请退货单明细批次信息", required = true)
    private List<B2BOrderReturnDetailBatchApplyForm> orderReturnDetailBatchList;
}
