package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:wei.wang
 * @date:2021/10/20
 */
@Data
public class OrderGoodsDetailVO extends OrderGoodsVO {

    /**
     * 明细id
     */
    @ApiModelProperty(value = "明细id")
    private Long detailId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品批准文号
     */
    @ApiModelProperty(value = "商品批准文号")
    private String goodsLicenseNo;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String goodsSpecification;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "商品生产厂家")
    private String goodsManufacturer;

    /**
     * 商品单价
     */
    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer goodsQuantity;

    /**
     * 发货数量
     */
    @ApiModelProperty(value = "发货数量")
    private Integer deliveryAllQuantity;

    /**
     * 批次信息
     */
    @ApiModelProperty(value = "批次信息")
    private List<OrderDeliveryVO> deliveryList;

    /**
     * 活动类型:0无,2:秒杀,3:特价,4:组合促销
     */
    @ApiModelProperty(value = "活动类型:0无,2:特价,3:秒杀,4:组合促销")
    private Integer promotionActivityType;

    /**
     * 组合套装最低数量
     */
    @ApiModelProperty(value = "组合套装最低数量")
    private Integer promotionNumber;
}

