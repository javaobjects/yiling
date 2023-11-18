package com.yiling.sales.assistant.app.order.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 查询订单信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderPageForm extends QueryPageListForm {

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    @NotNull
    private Long customerEid;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNo;


    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    /**
     * 订单状态：订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消 -30 待客户确认 -20 待付款
     */
    @ApiModelProperty(value = "订单状态：-50,未提交 -未提交,10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消 -30 待客户确认 -20 待付款 ")
    @NotNull
    private Integer orderStatus;


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


}
