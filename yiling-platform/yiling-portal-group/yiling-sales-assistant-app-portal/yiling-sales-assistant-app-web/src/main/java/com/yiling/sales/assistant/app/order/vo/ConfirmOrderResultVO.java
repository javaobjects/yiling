package com.yiling.sales.assistant.app.order.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 确认订单返回结果
 * @author zhigang.guo
 * @date: 2022/2/26
 */
@Data
@Accessors(chain = true)
public class ConfirmOrderResultVO implements Serializable {

    @ApiModelProperty(value = "订单号", required = true)
    private List<String> orderCodeList;

    @ApiModelProperty(value = "是否需要在线支付", required = true)
    private Boolean hasOnlinePay;

    @ApiModelProperty(value = "在线支付交易ID", required = true)
    private String payId;

    @ApiModelProperty(value = "在线支付金额", required = true)
    private BigDecimal payMoney;

    @ApiModelProperty(value = "买家EID", required = true)
    private Long buyerEid;

    @ApiModelProperty(value = "买家名称", required = true)
    private String buyerName;
}
