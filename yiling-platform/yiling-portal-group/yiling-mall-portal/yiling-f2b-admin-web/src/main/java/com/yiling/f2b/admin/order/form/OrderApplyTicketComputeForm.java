package com.yiling.f2b.admin.order.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 申请票折计算入参
 * @author:wei.wang
 * @date:2021/8/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderApplyTicketComputeForm extends BaseForm {
    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID",required = true)
    @NotNull
    private Long orderId;

    /**
     * 明细ID
     */
    @ApiModelProperty(value = "明细ID",required = true)
    @NotNull
    private Long detailId;

    /**
     * 出库单号
     */
    @ApiModelProperty(value = "出库单号",required = true)
    @NotNull
    private String erpDeliveryNo;

    /**
     * 票折方式
     */
    @ApiModelProperty(value = "票折方式：1-按比率 2-按金额",required = true)
    @NotNull
    private Integer invoiceDiscountType;

    /**
     * 金额值或者比率值
     */
    @ApiModelProperty(value = "金额值或者比率值",required = true)
    @NotNull
    private BigDecimal value;

    /**
     * 商品单价
     */
    @ApiModelProperty(value = "商品单价",required = true)
    @NotNull
    private BigDecimal goodsPrice;

    /**
     * 明细批次信息
     */
    @ApiModelProperty(value = "明细批次信息",required = true)
    @NotEmpty
    List<@Valid OrderApplyComputeBatchForm> batchFormList;

}
