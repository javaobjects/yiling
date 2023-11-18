package com.yiling.b2b.admin.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通用商品明细VO
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDetailVO extends BaseVO {

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 商品标准库ID
     */
    @ApiModelProperty(value = "商品标准库ID")
    private Long standardId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * goodsSkuId
     */
    @ApiModelProperty(value = "goodsSkuId")
    private Long goodsSkuId;

    /**
     * 包装数量
     */
    @ApiModelProperty(value = "包装数量")
    private Long packageNumber;

    /**
     * 商品备注
     */
    @ApiModelProperty(value = "商品备注")
    private String goodsRemark;


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
     * 商品单价
     */
    @ApiModelProperty(value = "商品原单价")
    private BigDecimal originalPrice;
    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer goodsQuantity;

    /**
     * 商品小计
     */
    @ApiModelProperty(value = "金额小计")
    private BigDecimal goodsAmount;

    /**
     * 发货数量
     */
    @ApiModelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String goodsPic;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal couponDiscountAmount;

    /**
     *支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal realAmount;

    /**
     * 活动类型:0无,2:秒杀,3:特价,4:组合促销
     */
    @ApiModelProperty(value = "活动类型:0-无 2-特价 3-秒杀 4-组合促销 6-预售")
    private Integer promotionActivityType;

    @ApiModelProperty(value = "单位")
    private String unit;
    /**
     * 套装基础数量
     */
    @ApiModelProperty("套装基础数量")
    private Integer promotionNumber;

}
