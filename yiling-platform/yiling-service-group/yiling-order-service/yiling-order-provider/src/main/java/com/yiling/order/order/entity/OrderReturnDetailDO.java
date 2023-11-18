package com.yiling.order.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退货单明细
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_return_detail")
public class OrderReturnDetailDO extends BaseDO {

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

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 更新人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
