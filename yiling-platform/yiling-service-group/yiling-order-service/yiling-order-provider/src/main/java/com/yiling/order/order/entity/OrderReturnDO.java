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
 * 退货单
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_return")
public class OrderReturnDO extends BaseDO {

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
     * 退货单号
     */
    private String returnNo;

    /**
     * 退货单来源 1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer returnSource;

    /**
     * 退货单类型：1-POP退货单,2-B2B退货单
     */
    private Integer orderReturnType;

    /**
     * 买家企业id
     */
    private Long buyerEid;

    /**
     * 买家名称
     */
    private String buyerEname;

    /**
     * 卖家企业id
     */
    private Long sellerEid;

    /**
     * 卖家名称
     */
    private String sellerEname;

    /**
     * 配送商企业ID
     */
    private Long distributorEid;

    /**
     * 配送商名称
     */
    private String distributorEname;

    /**
     * 发票节点
     */
    private Integer invoiceStatus;

    /**
     * 退货单类型：1-供应商退货单 2-破损退货单 3-采购退货单
     */
    private Integer returnType;

    /**
     * 退货单状态：1-待审核 2-审核通过 3-审核驳回
     */
    private Integer returnStatus;

    /**
     * 退货单审核人
     */
    private Long returnAuditUser;

    /**
     * 退货单审核时间
     */
    private Date returnAuditTime;

    /**
     * 驳回原因
     */
    private String failReason;

    /**
     * 退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 票折金额
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 平台优惠劵折扣金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家优惠劵折扣金额
     */
    private BigDecimal couponDiscountAmount;

    /**
     * 预售优惠金额
     */
    private BigDecimal presaleDiscountAmount;

    /**
     * 退回平台支付优惠金额
     */
    private BigDecimal platformPaymentDiscountAmount;

    /**
     * 退回商家支付优惠金额
     */
    private BigDecimal shopPaymentDiscountAmount;

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
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
