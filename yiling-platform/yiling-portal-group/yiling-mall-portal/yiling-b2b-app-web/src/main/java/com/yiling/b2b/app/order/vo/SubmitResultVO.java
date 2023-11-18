package com.yiling.b2b.app.order.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.b2b.app.order.vo
 * @date: 2021/10/26
 */
@Data
@Builder
@Accessors(chain = true)
public class SubmitResultVO implements Serializable {

    @ApiModelProperty(value = "订单号", required = true)
    private List<String> orderCodeList;

    @ApiModelProperty(value = "是否需要在线支付", required = true)
    private Boolean hasOnlinePay;

    @ApiModelProperty(value = "在线支付交易ID", required = true)
    private String payId;

    @ApiModelProperty(value = "在线支付金额", required = true)
    private BigDecimal payMoney;
}
