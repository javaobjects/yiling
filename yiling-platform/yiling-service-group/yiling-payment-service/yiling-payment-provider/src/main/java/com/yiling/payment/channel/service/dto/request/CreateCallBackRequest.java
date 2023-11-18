package com.yiling.payment.channel.service.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service.dto
 * @date: 2021/10/22
 */
@Data
@Accessors(chain=true)
public class CreateCallBackRequest {

    /**
     * 支付交易流水号
     */
    private String payNo;

    /**
     * 交易状态
     */
    private Integer tradeState;

    /**
     * 第三方会写流水单号
     */
    private String  thirdId;

    /**
     * 第三方会写支付状态
     */
    private String thirdState;

    /**
     * 银行参数
     */
    private String bank;

    /**
     * 错误信息
     */
    private String errorMessage;


}
