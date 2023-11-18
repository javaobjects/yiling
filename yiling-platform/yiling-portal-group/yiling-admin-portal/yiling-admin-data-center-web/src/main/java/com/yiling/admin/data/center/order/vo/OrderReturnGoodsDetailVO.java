package com.yiling.admin.data.center.order.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退货单明细表对应订单前端返回实体-数据补充
 *
 * @author: yong.zhang
 * @date: 2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderReturnGoodsDetailVO extends ReturnOrderDetailVO {


    @ApiModelProperty(value = "包装数量")
    private Long packageNumber;

    @ApiModelProperty(value = "商品备注")
    private String goodsRemark;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    @ApiModelProperty(value = "收货数量")
    private Integer receiveQuantity;

    @ApiModelProperty(value = "商品图片")
    private String goodsPic;

    @ApiModelProperty(value = "退货数量")
    private Integer returnQuantity;

    @ApiModelProperty(value = "应退金额")
    private BigDecimal totalReturnAmount;

    @ApiModelProperty(value = "折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "实际退款金额")
    private BigDecimal returnAmount;

    @ApiModelProperty(value = "商品对应批次号列表")
    private List<OrderReturnGoodsBatchVO> orderDeliveryList;
}
