package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建订单明细 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateOrderDetailRequest extends BaseRequest {

    private static final long serialVersionUID = 6733736984854570809L;

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 订单明细ID
     */
    private Long orderDetailId;

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
     * 商品SkUId
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
     * 商品原价
     */
    private BigDecimal originalPrice;

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
     * 商品定金小计
     */
    private BigDecimal depositAmount;

    /**
     * 商家优惠劵优惠金额
     */
    private BigDecimal couponDiscountAmount;

    /**
     * 商家支付优惠金额
     */
    private BigDecimal shopPaymentDiscountAmount;


    /**
     * 平台优惠劵优惠金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 平台支付促销优惠金额
     */
    private BigDecimal platformPaymentDiscountAmount;

    /**
     * 预订单优惠金额
     */
    private BigDecimal presaleDiscountAmount;

    /**
     * 打折促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 促销活动基价
     */
    private BigDecimal promotionActivityPrice;

    /**
     * 促销活动类型
     */
    private Integer promotionActivityType;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 现折优惠信息
     */
    private OrderDetailCashDiscountInfoDTO orderDetailCashDiscountInfoDTO;

}
