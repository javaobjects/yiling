package com.yiling.bi.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author baifc
 * @since 2022-09-19
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("ods_order_detail")
public class OdsOrderDetailDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单批次号
     */
    private String batchNo;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 买家名称
     */
    private String buyerEname;

    /**
     * 买家所属省份编码
     */
    private String buyerProvinceCode;

    /**
     * 买家所属城市编码
     */
    private String buyerCityCode;

    /**
     * 买家所属区域编码
     */
    private String buyerRegionCode;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;

    /**
     * 卖家名称
     */
    private String sellerEname;

    /**
     * 卖家ERP编码
     */
    private String sellerErpCode;

    /**
     * 配送商企业ID
     */
    private Long distributorEid;

    /**
     * 配送商名称
     */
    private String distributorEname;

    /**
     * 客户ERP编码
     */
    private String customerErpCode;

    /**
     * 商品总金额
     */
    private BigDecimal totalAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 票折单号（多个用英文逗号分隔）
     */
    private String ticketDiscountNo;

    /**
     * 票折金额
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 运费
     */
    private BigDecimal freightAmount;

    /**
     * 平台优惠劵金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家优惠券金额
     */
    private BigDecimal couponDiscountAmount;

    /**
     * 退货商品金额
     */
    private BigDecimal returnAmount;

    /**
     * 退货现折金额
     */
    private BigDecimal returnCashDicountAmount;

    /**
     * 退货票折金额
     */
    private BigDecimal returnTicketDiscountAmount;

    /**
     * 退回平台优惠券金额
     */
    private BigDecimal returnPlatformCouponDiscountAmount;

    /**
     * 退回商家优惠劵金额
     */
    private BigDecimal returnCouponDiscountAmount;

    /**
     * 应付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 支付类型：1-线下支付 2-在线支付
     */
    private Integer paymentType;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 在线支付渠道
     */
    private String payChannel;

    /**
     * 在线支付来源
     */
    private String paySource;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    private Integer paymentStatus;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 客户确认状态-20-未转发,-30-待客户确认,-40-已确认
     */
    private Integer customerConfirmStatus;

    /**
     * 客户确认状态确认时间
     */
    private Date customerConfirmTime;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 订单备注
     */
    private String orderNote;

    /**
     * 物流类型：1-自有物流 2-第三方物流
     */
    private Integer deliveryType;

    /**
     * 物流公司
     */
    private String deliveryCompany;

    /**
     * 物流单号
     */
    private String deliveryNo;

    /**
     * 商务联系人ID
     */
    private Long contacterId;

    /**
     * 商务联系人姓名
     */
    private String contacterName;

    /**
     * 商务联系人部门ID
     */
    private Long departmentId;

    /**
     * 省区经理ID
     */
    private Long provinceManagerId;

    /**
     * 省区经理工号
     */
    private String provinceManagerCode;

    /**
     * 下单人
     */
    private Long createUser;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 审核状态： 1-未提交 2-待审核 3-审核通过 4-审核驳回 
     */
    private Integer auditStatus;

    /**
     * 审核人
     */
    private Long auditUser;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核驳回原因
     */
    private String auditRejectReason;

    /**
     * ERP推送销售订单状态：1-未推送 2-推送成功 3-推送失败 【已废弃】4-eas提取成功【已废弃】 5-eas提取失败【已废弃】 6-读取成功【已废弃】
     */
    private Integer erpPushStatus;

    /**
     * ERP推送时间
     */
    private Date erpPushTime;

    /**
     * ERP推送备注
     */
    private String erpPushRemark;

    /**
     * ERP订单号【已废弃】
     */
    private String erpOrderNo;

    /**
     * 【已废弃】ERP出库单号
     */
    private String erpDeliveryNo;

    /**
     * 【已废弃】ERP应收单号
     */
    private String erpReceivableNo;

    /**
     * 【已废弃】ERP应收单状态：1-有效 2-无效 3-部分删除
     */
    private Integer erpReceivableStatus;

    /**
     * 发货人
     */
    private Long deliveryUser;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 收货人
     */
    private Long receiveUser;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 开票时间
     */
    private Date invoiceTime;

    /**
     * 开票状态：1-待申请 2-申请中  4-部分开票【废弃】 3-已开票 5-已作废 6-不开票
     */
    private Integer invoiceStatus;

    /**
     * 开票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 是否经过审核修改：0-否 1-是
     */
    private Integer auditModifyFlag;

    /**
     * 审核修改时间
     */
    private Date auditModifyTime;

    /**
     * ERP推送采购出库状态：1-未推送 2-推送成功 3-推送失败 4-eas提取成功 5-eas提取失败
     */
    private Integer erpDeliveryStatus;

    /**
     * ERP推送出库时间
     */
    private Date erpDeliveryTime;

    /**
     * ERP推送出库备注
     */
    private String erpDeliveryRemark;

    /**
     * ERP推送采购状态：1-未推送 2-推送成功 3-推送失败 【已废弃】
     */
    private Integer erpPurchaseStatus;

    /**
     * ERP推送采购单时间
     */
    private Date erpPurchaseTime;

    /**
     * ERP推送采购单备注
     */
    private String erpPurchaseRemark;

    /**
     * 【已废弃】是否生成结算单(1-未生成,2-已生成)
     */
    private Integer isSettlementOrder;

    /**
     * 【已废弃】货款结算状态(1-未结算,2-已结算)
     */
    private Integer goodsSettlementStatus;

    /**
     * 【已废弃】促销结算状态(1-未结算,2-已结算)
     */
    private Integer saleSettlementStatus;

    /**
     * 合同编号
     */
    private String contractNumber;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 商品标准库ID
     */
    private Long standardId;

    /**
     * 商品SKUID
     */
    private Long goodsSkuId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品ERP编码
     */
    private String goodsErpCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 购买商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 购买商品现折金额
     */
    private BigDecimal changecashDiscountAmount;

    /**
     * 购买商品票折金额
     */
    private BigDecimal changeticketDiscountAmount;

    /**
     * 平台优惠劵折扣金额
     */
    private BigDecimal changeplatformCouponDiscountAmount;

    /**
     * 商家优惠劵折扣金额
     */
    private BigDecimal changecouponDiscountAmount;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 发货商品小计
     */
    private BigDecimal deliveryAmount;

    /**
     * 发货商品的现折金额
     */
    private BigDecimal deliveryCashDiscountAmount;

    /**
     * 发货商品的票折金额
     */
    private BigDecimal deliveryTicketDiscountAmount;

    /**
     * 发货商品的平台优惠劵折扣金额
     */
    private BigDecimal deliveryPlatformCouponDiscountAmount;

    /**
     * 发货商品的商家优惠劵金额
     */
    private BigDecimal deliveryCouponDiscountAmount;

    /**
     * 卖家退货数量
     */
    private Integer sellerReturnQuantity;

    /**
     * 卖家退货商品小计
     */
    private BigDecimal sellerReturnAmount;

    /**
     * 卖家退货商品的现折金额
     */
    private BigDecimal sellerReturnCashDiscountAmount;

    /**
     * 卖家退货商品的票折金额
     */
    private BigDecimal sellerReturnTicketDiscountAmount;

    /**
     * 卖家商品的平台优惠劵折扣金额
     */
    private BigDecimal sellerPlatformCouponDiscountAmount;

    /**
     * 卖家商品的商家优惠劵金额
     */
    private BigDecimal sellerCouponDiscountAmount;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;

    /**
     * 收货商品小计
     */
    private BigDecimal receiveAmount;

    /**
     * 收货商品的现折金额
     */
    private BigDecimal receiveCashDiscountAmount;

    /**
     * 收货商品的票折金额
     */
    private BigDecimal receiveTicketDiscountAmount;

    /**
     * 收货商品的商家优惠劵金额
     */
    private BigDecimal receiveCouponDiscountAmount;

    /**
     * 收货平台的商家优惠劵金额
     */
    private BigDecimal receivePlatformCouponDiscountAmount;

    /**
     * 退货数量
     */
    private Integer returnQuantity;

    /**
     * 退货商品小计
     */
    private BigDecimal changereturnAmount;

    /**
     * 退货商品的现折金额
     */
    private BigDecimal returnCashDiscountAmount;

    /**
     * 退货商品的票折金额
     */
    private BigDecimal changereturnTicketDiscountAmount;

    /**
     * 退货商品的平台优惠劵折扣金额
     */
    private BigDecimal changereturnPlatformCouponDiscountAmount;

    /**
     * 退货商品的商家优惠劵折扣金额
     */
    private BigDecimal changereturnCouponDiscountAmount;

    private Date extractTime;

    private Integer changedetailId;

    /**
     * 抽取标识
     */
    private String flagfield;

    /**
     * ID
     */
    @TableId(value = "datailid", type = IdType.AUTO)
    private Long datailid;

    /**
     * 订单ID
     */
    private Long datailorderId;

    /**
     * 订单号
     */
    private String datailorderNo;

    /**
     * 商品标准库ID
     */
    private Long datailstandardId;

    /**
     * 商品类型，0，普通，1，以岭品
     */
    private Integer goodsType;

    /**
     *  配送商商品ID
     */
    private Long distributorGoodsId;

    /**
     *  商品ID
     */
    private Long datailgoodsId;

    /**
     *  商品编码
     */
    private String goodsCode;

    /**
     * 商品ERP编码
     */
    private String datailgoodsErpCode;

    /**
     *  商品名称
     */
    private String datailgoodsName;

    /**
     *  商品通用名
     */
    private String goodsCommonName;

    /**
     *  商品批准文号
     */
    private String goodsLicenseNo;

    /**
     *  商品规格
     */
    private String datailgoodsSpecification;

    /**
     * 商品生产厂家
     */
    private String goodsManufacturer;

    /**
     * 商品单价
     */
    private BigDecimal datailgoodsPrice;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 限定价格，以岭限价
     */
    private BigDecimal limitPrice;

    /**
     * 购买数量
     */
    private Integer datailgoodsQuantity;

    /**
     * 商品小计
     */
    private BigDecimal datailgoodsAmount;

    /**
     * 现折金额
     */
    private BigDecimal datailcashDiscountAmount;

    /**
     *  商品skuID
     */
    private Long datailgoodsSkuId;

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 促销价格
     */
    private BigDecimal promotionGoodsPrice;

    /**
     *  活动类型：0，无，2，特价，3，秒杀，4，组合促销
     */
    private Integer promotionActivityType;


}
