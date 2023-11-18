package com.yiling.payment.pay.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.payment.enums.PaySourceEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.dto.request
 * @date: 2021/10/15
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Accessors(chain=true)
public class CreatePayTradeRequest extends BaseRequest {

    private static final long     serialVersionUID = -2020365601797945602L;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 支付来源
     */
    private String paySource = PaySourceEnum.YEE_PAY_BANK.getSource();

    /**
     * 支付交易ID
     */
    private String payId;

    /**
     * 操作平台。POP-PC平台：POP-PC；POP-APP平台：POP-APP；B2B-APP平台：B2B-APP；销售助手-APP平台：SA-APP；系统：sys
     */
    private String tradeSource;

    /**
     * 用户IP
     */
    private String userIp;

	/**
	 * 微信openId，当paySource为yeePayWechat时必填
	 */
	private String openId;

    /**
     * 支付成功请求url地址
     */
	private String redirectUrl;

    /**
     * 微信支付appId
     */
	private String appId;

}
