package com.yiling.b2b.admin.order.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 批次信息VO
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDeliveryVO extends BaseVO {
    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 订单明细ID
     */
    @ApiModelProperty(value = "订单明细ID")
    private Long detailId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNo;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private Date expiryDate;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer goodsQuantity;

    /**
     * 发货数量
     */
    @ApiModelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    /**
     * 收货数量
     */
    @ApiModelProperty(value = "收货数量")
    private Integer receiveQuantity;

    /**
     * 退货数量
     */
    @ApiModelProperty(value = "可退货数量")
    private Integer remainReturnQuantity;

}
