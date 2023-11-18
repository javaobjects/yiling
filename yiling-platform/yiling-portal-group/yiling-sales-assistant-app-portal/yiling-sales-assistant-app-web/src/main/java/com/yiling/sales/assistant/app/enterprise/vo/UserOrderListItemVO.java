package com.yiling.sales.assistant.app.enterprise.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 客户订单列表 VO
 *
 * @author: lun.yu
 * @date: 2021/9/24
 */
@Builder
@Data
public class UserOrderListItemVO {

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("下单时间")
    private Date orderCreateTime;

    @ApiModelProperty("商品类别数量")
    private Integer typeNum;

    @ApiModelProperty("商品总数量")
    private Long goodsNum;

}
