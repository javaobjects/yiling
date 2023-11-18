package com.yiling.order.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("`order`")
public class OrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
     * 客户内码
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
     * 票折单号
     */
    private String ticketDiscountNo;

    /**
     * 票折金额
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 平台优惠券金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家优惠劵金额
     */
    private BigDecimal couponDiscountAmount;

    /**
     * 预售优惠金额
     */
    private BigDecimal presaleDiscountAmount;

    /**
     * 商家支付优惠金额
     */
    private BigDecimal shopPaymentDiscountAmount;

    /**
     * 平台支付优惠总金额
     */
    private BigDecimal platformPaymentDiscountAmount;

    /**
     * 运费
     */
    private BigDecimal freightAmount;

    /**
     * 退货商品金额
     */
    private  BigDecimal returnAmount;

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
     * 预售退回优惠金额
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

    /**
     * 应付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 支付类型：1-线下支付 2-在线支付
     */
    private Integer paymentType;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    private Integer paymentMethod;

    /**
     * 在线支付渠道
     */
    private String payChannel;

    /**
     * 在线支付方式
     */
    private String paySource;

    /**
     * 支付状态：1-待支付 2-部分支付 3-已支付
     * {@link com.yiling.order.order.enums.PaymentStatusEnum}
     */
    private Integer paymentStatus;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 订单类型：1-POP订单
     */
    private Integer orderType;

    /**
     * 订单类别
     * {@link com.yiling.order.order.enums.OrderCategoryEnum}
     */
    private Integer orderCategory;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 客户确认状态-20-未转发,-30-待客户确认,-40-已确认
     */
    private Integer customerConfirmStatus;

    /**
     * 客户确认时间
     */
    private Date customerConfirmTime;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 下单备注
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
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 下单时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 审核状态：1-未提交 2-待审核 3-审核通过 4-审核驳回
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
     * ERP推送状态：1-未推送 2-推送成功 3-推送失败
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
     * ERP订单号
     */
    private String erpOrderNo;

    /**
     * ERP应收单状态：1-有效 2-无效
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
     * 开票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
     */
    private Integer invoiceStatus;

    /**
     * 开票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 订单是否经过反审核标识 0否，1是
     */
    private Integer auditModifyFlag;

    /**
     * 订单反审核时间
     */
    private Date auditModifyTime;

    /**
     * ERP推送出库状态：1-未推送 2-推送成功 3-推送失败 4-eas提取成功 5-eas提取失败
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
     * ERP推送出库状态：1-未推送 2-推送成功 3-推送失败
     */
    private Integer erpPurchaseStatus;

    /**
     * ERP推送采购单时间
     */
    private Date erpPurchaseTime;

    /**
     * ERP推送采购备注
     */
    private String erpPurchaseRemark;

    /**
     * 合同编号
     */
    private String contractNumber;

    /**
     * 隐藏订单展示标记(1-不隐藏,2-隐藏)
     */
    private Integer hideFlag;

}
