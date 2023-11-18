package com.yiling.hmc.payment.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhigang.guo
 * @date: 2023/2/24
 */
@Data
public class QueryResultVO {

    @ApiModelProperty(value = "是否支付成功")
    private Boolean isPaySuccess;

    @ApiModelProperty("在线支付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("支付订单号")
    private List<String> orderNoList;

    @ApiModelProperty("支付订单Ids")
    private List<Long> orderIdList;

    @ApiModelProperty("支付平台")
    private String orderPlatform;

    @ApiModelProperty("交易类型")
    private Integer tradeType;
}
