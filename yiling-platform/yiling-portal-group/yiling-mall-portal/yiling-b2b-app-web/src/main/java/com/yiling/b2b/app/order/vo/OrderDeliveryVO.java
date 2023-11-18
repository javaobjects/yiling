package com.yiling.b2b.app.order.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单发货信息
 * @author:wei.wang
 * @date:2021/11/5
 */
@Data
public class OrderDeliveryVO {
    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private Date expiryDate;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNo;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "发货购买数量")
    private Integer deliveryQuantity;

    /**
     * 退货数量
     */
    @ApiModelProperty(value = "退货数量")
    private Integer returnQuantity;
}
