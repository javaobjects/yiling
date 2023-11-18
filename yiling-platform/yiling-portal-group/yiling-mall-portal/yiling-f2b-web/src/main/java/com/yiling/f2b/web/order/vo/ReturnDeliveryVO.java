package com.yiling.f2b.web.order.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退货批次的发货信息数据
 *
 * @author: yong.zhang
 * @date: 2022/2/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReturnDeliveryVO extends BaseVO {

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单明细ID")
    private Long detailId;

    @ApiModelProperty(value = "商品标准库ID")
    private Long standardId;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品ERP编码")
    private String goodsErpCode;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "有效期")
    private Date expiryDate;

    @ApiModelProperty(value = "生产日期")
    private Date produceDate;

    @ApiModelProperty(value = "购买数量")
    private Integer goodsQuantity;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    @ApiModelProperty(value = "收货数量")
    private Integer receiveQuantity;

    @ApiModelProperty(value = "备注")
    private String remark;
}
