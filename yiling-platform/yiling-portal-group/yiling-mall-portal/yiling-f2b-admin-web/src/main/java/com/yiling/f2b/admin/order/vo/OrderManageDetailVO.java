package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审核订单明细VO
 * @author:wei.wang
 * @date:2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderManageDetailVO extends BaseVO {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     * 审核状态：1-未提交 2-待审核 3-审核通过 4-审核驳回
     */
    @ApiModelProperty(value = "审核状态：1-未提交 2-待审核 3-审核通过 4-审核驳回")
    private Integer auditStatus;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    @ApiModelProperty(value = "支付状态：1-待支付 2-已支付")
    private Integer paymentStatus;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "商品总金额")
    private BigDecimal totalAmount;

    /**
     * 现折金额
     */
    @ApiModelProperty(value = "现折金额")
    private BigDecimal cashDiscountAmount;

    /**
     * 现折金额
     */
    @ApiModelProperty(value = "折扣总金额")
    private BigDecimal discountAmount;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal paymentAmount;

    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String orderNote;

    /**
     * 物流类型：1-自有物流 2-第三方物流
     */
    @ApiModelProperty(value = "物流类型：1-自有物流 2-第三方物流")
    private Integer deliveryType;

    /**
     * 收货地址信息
     */
    @ApiModelProperty(value = "收货地址信息")
    private OrderAddressVO orderAddress;


    /**
     * 购销合同url
     */
    @ApiModelProperty(value = "购销合同url")
    private List<String> orderContractUrl;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private List<OrderDetailVO> orderDetailList;

    /**
     * 账期信息
     */
    @ApiModelProperty(value = "账期信息")
    private PaymentDaysInfoVO paymentDaysInfo;

    /**
     * 审核驳回原因
     */
    @ApiModelProperty(value = "审核驳回原因")
    private String auditRejectReason;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNumber;

    @ApiModelProperty("部门名称")
    private String departmentName;

    /**
     * 部门名称Id
     */
    @ApiModelProperty("部门名称Id")
    private Long departmentId;

    @ApiModelProperty("商务联系人名称")
    private String contacterName;

    /**
     * 卖家企业ID
     */
    @ApiModelProperty(value = "卖家企业ID")
    private Long sellerEid;

    /**
     * 卖家名称
     */
    @ApiModelProperty(value = "卖家名称")
    private String sellerEname;

    /**
     * 采购商名称
     */
    @ApiModelProperty(value = "采购商名称")
    private String buyerEname;

    /**
     * 采购商Eid
     */
    @ApiModelProperty(value = "采购商Eid")
    private Long buyerEid;

}
