package com.yiling.f2b.admin.order.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询订单列表form对象
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderInfoForm extends QueryPageListForm {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;

    /**
     * 开始下单时间
     */
    @ApiModelProperty(value = "开始下单时间")
    private Date startCreateTime;

    /**
     * 结束下单时间
     */
    @ApiModelProperty(value = "结束下单时间")
    private Date endCreateTime;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    @ApiModelProperty(value = "支付状态：1-待支付 2-已支付")
    private Integer paymentStatus;

    @ApiModelProperty(value = "订单类型：1-销售订单 2-采购订单")
    private Integer type;

    /**
     * 发票状态
     */
    @ApiModelProperty(value = "发票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废")
    private Integer invoiceStatus;

    /**
     *订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long id;

    /**
     * 部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门
     */
    @ApiModelProperty("部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门")
    private Integer departmentType;
}
