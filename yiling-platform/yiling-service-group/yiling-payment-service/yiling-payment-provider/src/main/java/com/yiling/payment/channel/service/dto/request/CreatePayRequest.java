package com.yiling.payment.channel.service.dto.request;

import java.math.BigDecimal;

import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.TradeTypeEnum;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service.dto
 * @date: 2021/10/20
 */
@Data
@Accessors(chain=true)
public class CreatePayRequest {

    /**
     * userId
     */
    private Long userId;

    /**
     * 交易ID
     */
    private String payId;

    /**
     * 交易编码
     */
    private String payNo;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 支付来源:app,pc
     */
    private String paySource;

    /**
     * 交易类型
     */
    private TradeTypeEnum tradeTypeEnum;

    /**
     * 订单交易平台
     */
    private OrderPlatformEnum orderPlatformEnum;

	/**
	 * userIp
	 */
	private String userIp;

	/**
	 * 微信openId，当paySource为yeePayWechat时必填
	 */
	private String openId;

    /**
     * 微信appId，当paySource为yeePayWechat时必填
     */
	private String appId;

    /**
     * 支付成功请求url地址
     */
    private String redirectUrl;


    /**
     * 对账备注
     */
    private String remark;

}
