package com.yiling.hmc.usercenter.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 市场订单VO
 * </p>
 *
 * @author fan.shen
 * @date 2023-02-17
 */
@Data
public class MarketOrderVO extends BaseVO {

    @ApiModelProperty("平台订单编号")
    private String orderNo;

    @ApiModelProperty("订单状态:1-待确认/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退")
    private Integer orderStatus;

    @ApiModelProperty("支付票据")
    private String payTicket;
}
