package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退货单
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnDTO extends BaseDO {

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
     * 退货单来源1-POP 2-销售助手 3-B2B
     */
    private Integer returnSource;

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
     * 发票节点
     */
    private Integer invoiceStatus;

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
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    //============================================注意：下面字段不是数据库字段，返回前端页面需要=======================================================

    /**
     * 退款总金额
     */
    private BigDecimal totalReturnAmount;

    /**
     * 优惠总金额
     */
    private BigDecimal totalDiscountAmount;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    private Integer paymentMethod;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    private Integer paymentStatus;

    /**
     * 退货商品件数
     */
    private Integer returnGoods;

    /**
     * 退货商品总数量
     */
    private Integer returnGoodsNum;


    /**
     * 商品明细组装集合
     */
    private List<OrderReturnGoodsDetailDTO> orderReturnGoodsDetailList;

    /**
     * 商品明细集合
     */
    private List<OrderReturnDetailDTO> OrderReturnDetailDTOList;


}
