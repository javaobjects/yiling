package com.yiling.b2b.admin.order.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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
public class B2BOrderReturnDetailApplyForm {

    @ApiModelProperty(value = "订单明细Id", required = true)
    @NotNull(message = "订单明细Id不能为空")
    private Long detailId;

    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private Long goodsId;

    @ApiModelProperty(value = "商品skuID", required = true)
    @NotNull(message = "商品skuID不能为空")
    private Long goodsSkuId;

    @ApiModelProperty(value = "申请退货单明细批次信息", required = true)
    @NotEmpty(message = "申请退货单明细批次信息不能为空")
    private List<@Valid B2BOrderReturnDetailBatchApplyForm> orderReturnDetailBatchList;
}
