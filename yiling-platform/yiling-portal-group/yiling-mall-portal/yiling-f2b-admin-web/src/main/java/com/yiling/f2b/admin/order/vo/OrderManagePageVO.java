package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审核订单列表VO
 *
 * @author:wei.wang
 * @date:2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderManagePageVO extends BaseVO {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消 -20-驳回")
    private Integer orderStatus;

    /**
     * 审核状态：1-未提交 2-待审核 3-审核通过 4-审核驳回
     */
    @ApiModelProperty(value = "审核状态：1-未提交 2-待审核 3-审核通过 4-审核驳回")
    private Integer auditStatus;

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

    /**
     * 省市区
     */
    @ApiModelProperty(value = "省市区")
    private String buyerAddress;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    /**
     * 支付状态
     */
    @ApiModelProperty(value = "支付状态：1-待支付 2-已支付")
    private Integer paymentStatus;

    /**
     * 货款金额
     */
    @ApiModelProperty(value = "货款金额")
    private BigDecimal totalAmount;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal paymentAmount;

    /**
     * 购买商品种类
     */
    @ApiModelProperty(value = "购买商品种类")
    private Integer goodsOrderNum;

    /**
     * 购买商品件数
     */
    @ApiModelProperty(value = "购买商品件数")
    private Integer goodsOrderPieceNum;

    /**
     * 折扣总金额
     */
    @ApiModelProperty(value = "折扣总金额")
    private BigDecimal discountAmount;

    /**
     * 下单备注
     */
    @ApiModelProperty(value = "下单备注")
    private String orderNote;

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
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNumber;

    @ApiModelProperty("是否已经包含其它属性")
    private OrderManageDisableVO orderManageDisableVO;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String departmentName;

    /**
     * 部门名称Id
     */
    @ApiModelProperty("部门名称Id")
    private Long departmentId;

    @ApiModelProperty("商务联系人名称")
    private String contacterName;
}
