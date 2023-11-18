package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 预订单详情
 * @author:wei.wang
 * @date:2021/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderExpectDetailVO  extends BaseVO {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     * 审核状态：1-未提交 2-待审核 3-审核通过 4-审核驳回
     */
    @ApiModelProperty(value = "审核状态：1-未提交 2-待审核 3-审核通过 4-审核驳回")
    private Integer auditStatus;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "货款总金额")
    private BigDecimal totalAmount;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "支付总金额")
    private BigDecimal paymentAmount;

    /**
     * 审核人Id
     */
    @ApiModelProperty(value = "审核人Id")
    private Long auditUser;

    /**
     * 审核人名称
     */
    @ApiModelProperty(value = "审核人名称")
    private String auditUserName;

    /**
     * 审核人时间
     */
    @ApiModelProperty(value = "审核人时间")
    private Date auditTime;



    /**
     * 商务联系人id
     */
    private Long contacterId;

    /**
     * 商务联系人
     */
    @ApiModelProperty(value = "商务联系人")
    private String contacterName;

    /**
     * 商务联系人电话
     */
    @ApiModelProperty(value = "商务联系人电话")
    private String contacterTelephone;

    /**
     * 折扣金额
     */
    @ApiModelProperty(value = "折扣总金额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "供应商信息")
    private EnterpriseInfoVO enterpriseDistributorInfo;

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
     *  商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private List<OrderDetailVO> orderDetailList;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String orderNote;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNumber;

}
