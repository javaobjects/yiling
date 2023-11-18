package com.yiling.f2b.web.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退货单明细批次信息返回给前端的实体数据-补充
 *
 * @author: yong.zhang
 * @date: 2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderReturnGoodsBatchVO extends ReturnDeliveryVO {

    @ApiModelProperty(value = "批次ID")
    private Long id;

    @ApiModelProperty(value = "退货数量")
    private Integer returnQuantity;

    @ApiModelProperty(value = "允许退货数量")
    private Integer allowReturnQuantity;

    @ApiModelProperty(value = "退货单明细ID")
    private Long orderReturnDetailId;
}
