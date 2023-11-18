package com.yiling.order.order.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单明细
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_detail")
public class OrderDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商品标准库ID
     */
    private Long standardId;

    /**
     * 商品类型
     */
    private Integer goodsType;

    /**
     * 配送商商品ID
     */
    private Long distributorGoodsId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品skuId
     */
    private Long goodsSkuId;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品ERP编码
     */
    private String goodsErpCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品通用名
     */
    private String goodsCommonName;

    /**
     * 商品批准文号
     */
    private String goodsLicenseNo;

    /**
     * 商品规格
     */
    private String goodsSpecification;

    /**
     * 商品生产厂家
     */
    private String goodsManufacturer;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 商品原始价格
     */
    private BigDecimal originalPrice;

    /**
     * 促销活动单价
     */
    private BigDecimal promotionGoodsPrice;

    /**
     * 限定价格
     */
    private BigDecimal limitPrice;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 预售定金金额
     */
    private BigDecimal depositAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 促销活动ID(折扣价活动预留)
     */
    private Long promotionActivityId;

    /**
     * 促销活动类型
     * @see com.yiling.order.order.enums.PromotionActivityTypeEnum
     */
    private Integer promotionActivityType;


}
