package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * B2B移动端订单商品信息
 * @author:wei.wang
 * @date:2021/10/20
 */
@Data
public class OrderGoodsVO extends SimpleGoodsVO {

    /**
     * 商品skuID
     */
    @ApiModelProperty(value = "商品skuID")
    private Long goodsSkuId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String goodPic;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer goodsQuantity;

    /**
     * 购物车ID
     */
    @ApiModelProperty(value = "购物车ID")
    private Long cartId;

    /**
     * 组合包名称
     */
    @ApiModelProperty("组合包名称")
    private String packageName;

    /**
     * 组合包简称
     */
    @ApiModelProperty("组合包简称")
    private String packageShortName;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    /**
     * 优惠劵优惠金额
     */
    @ApiModelProperty(hidden = true,value = "优惠劵优惠金额" )
    @JsonIgnore
    private BigDecimal couponDiscountMoney;


    @ApiModelProperty(hidden = true,value = "支付分摊优惠金额" )
    @JsonIgnore
    private BigDecimal paymentDiscountMoney;


    /**
     * 促销活动ID
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long    promotionActivityId;

    /**
     * 促销活动类型
     * @see com.yiling.order.order.enums.PromotionActivityTypeEnum
     */
    @ApiModelProperty("促销活动类型：0正常商品,2,打折商品,3,秒杀商品,4,组合营销商品,5支付促销,6预售促销")
    private Integer promotionActivityType;

    /**
     * 套装基础数量
     */
    @ApiModelProperty("套装基础数量")
    private Integer promotionNumber;


}
