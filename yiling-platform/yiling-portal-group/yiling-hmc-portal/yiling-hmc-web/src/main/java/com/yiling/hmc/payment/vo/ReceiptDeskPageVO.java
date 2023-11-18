package com.yiling.hmc.payment.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收营台页面 VO
 *
 */
@Data
public class ReceiptDeskPageVO {

    @ApiModelProperty(value = "是否支付成功")
    private Boolean isPaySuccess;

    @ApiModelProperty(value = "交易类型(1:定金,2:支付,3:在线还款,4:尾款,5:会员")
    private Integer tradeType;

    @ApiModelProperty("在线支付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("支付交易流水Id")
    private String payId;

    @ApiModelProperty("交易失效倒计时间")
    private String remainTime;

    @ApiModelProperty("支付订单号")
    private List<String> orderNoList;

    @ApiModelProperty("支付订单Ids")
    private List<Long> orderIdList;

    @ApiModelProperty("支付渠道列表")
    private List<PaymentChannelVO> paymentChannelList;

    @Data
    public static class PaymentChannelVO {

        @ApiModelProperty("支付方式名称")
        private String name;

        @ApiModelProperty("支付方式编码")
        private String code;

        @ApiModelProperty("图片路径")
        private String url;
    }
}
