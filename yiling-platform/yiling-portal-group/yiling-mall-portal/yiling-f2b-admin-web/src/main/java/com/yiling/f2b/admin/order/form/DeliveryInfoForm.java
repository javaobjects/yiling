package com.yiling.f2b.admin.order.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发货批次明细from
 * @author:wei.wang
 * @date:2021/6/25
 */
@Data
public class DeliveryInfoForm implements java.io.Serializable {

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号",required = true)
    private String batchNo;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期",required = true)
    private Date expiryDate;

    /**
     * 发货数量
     */
    @ApiModelProperty(value = "发货数量",required = true)
    @NotNull(message = "不能为空")
    private Integer deliveryQuantity;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期",required = true)
    private Date produceDate;

}
