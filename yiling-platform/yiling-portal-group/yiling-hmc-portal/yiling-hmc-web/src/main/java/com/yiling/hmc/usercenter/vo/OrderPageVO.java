package com.yiling.hmc.usercenter.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
public class OrderPageVO extends BaseVO {

    @ApiModelProperty("平台订单编号")
    private String orderNo;

    @ApiModelProperty("购买保险id")
    private Long insuranceRecordId;

    @ApiModelProperty("下单时间")
    private Date orderTime;

    @ApiModelProperty("订单额总额")
    private BigDecimal totalAmount;

    @ApiModelProperty("订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退")
    private Integer orderStatus;

    @ApiModelProperty("共N件商品")
    private Integer goodsCount;



}
