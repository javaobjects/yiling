package com.yiling.order.order.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退货单明细
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnDetailDTO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 退货单ID
     */
    private Long returnId;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 商品标准库ID
     */
    private Long standardId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品skuId
     */
    private Long goodsSkuId;

    /**
     * 商品ERP编码
     */
    private String goodsErpCode;

//    /**
//     * 批次号
//     */
//    private String batchNo;

    /**
     * 退货数量
     */
    private Integer returnQuantity;

    /**
     * 退货商品小计
     */
    private BigDecimal returnAmount;

    /**
     * 退货商品的现折金额
     */
    private BigDecimal returnCashDiscountAmount;

    /**
     * 退货商品的票折金额
     */
    private BigDecimal returnTicketDiscountAmount;

    /**
     * 平台优惠劵折扣金额
     */
    private BigDecimal returnPlatformCouponDiscountAmount;

    /**
     * 商家优惠劵折扣金额
     */
    private BigDecimal returnCouponDiscountAmount;

    /**
     * 退货商品的预售优惠金额
     */
    private BigDecimal returnPresaleDiscountAmount;

    /**
     * 退回平台支付优惠金额
     */
    private BigDecimal returnPlatformPaymentDiscountAmount;

    /**
     * 退回商家支付优惠金额
     */
    private BigDecimal returnShopPaymentDiscountAmount;

//    /**
//     * ERP出库单号
//     */
//    private String erpDeliveryNo;
}
