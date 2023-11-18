package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderFlowVO extends BaseVO {

    @ApiModelProperty(value = "订单明细")
    private Long detailId;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品内码")
    private String goodsInSn;

    @ApiModelProperty(value = "供应商商名称")
    private String sellerEname;

    @ApiModelProperty(value = "采购商名称")
    private String buyerEname;

    @ApiModelProperty(value = "采购商渠道ID")
    private Long buyerChannelId;

    @ApiModelProperty(value = "采购商渠道名称")
    private String buyerChannelName;

    @ApiModelProperty(value = "批准文号")
    private String licenseNo;

    @ApiModelProperty(value = "商品规格")
    private String sellSpecifications;

    @ApiModelProperty(value = "商品单位")
    private String sellUnit;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "购买数量")
    private Integer goodsQuantity;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    /**
     * 退回退货数量
     */
    @ApiModelProperty(value = "退回退货数量")
    private Integer returnQuantity;

    /**
     * 采购商退货数量
     */
    @ApiModelProperty(value = "采购商退货数量")
    private Integer buyerReturnQuantity;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "商品小计")
    private BigDecimal goodsAmount;

    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;
}
