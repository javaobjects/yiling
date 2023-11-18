package com.yiling.payment.channel.yee.request;

import java.math.BigDecimal;
import java.util.Date;

import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.common.util.str.StringUtils;

import lombok.Data;

/**
 * 支付订单信息
 *
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2016/10/19 22:34
 *  </pre>
 */
@Data
public class YeePayOrder extends AssistOrder {

	/**
	 * 商品名称
	 */
	private String     subject;
	/**
	 * 商品描述
	 */
	private String     body;
	/**
	 * 附加信息
	 */
	private String     addition;
	/**
	 * 价格
	 */
	private BigDecimal price;

	/**
	 * 银行卡类型
	 */
	private String bankType;

	/**
	 * 支付创建ip
	 */
	private String userIp;

	/**
	 * 用户唯一标识 userId,用于绑定银行卡
	 */
	private String userId;

	/**
	 * 微信openId，用于聚合支付-微信下单
	 */
	private String openId;

	/**
	 * 支付币种
	 */
	private CurType curType;

	/**
	 * 订单过期时间
	 */
	private Date expirationTime;

    /**
     * 支付成功请求url地址
     */
    private String redirectUrl;

    /**
     * 微信支付appId
     */
    private String appId;


	public YeePayOrder() {
	}


	public YeePayOrder(String subject, String body, BigDecimal price, String outTradeNo, Long userId, String userIp) {
		this.subject = StringUtils.tryTrim(subject);
		this.body = StringUtils.tryTrim(body);
		this.price = price;
		this.userId = userId.toString();
		this.userIp = userIp;
		setOutTradeNo(StringUtils.tryTrim(outTradeNo));
	}

	public YeePayOrder(String subject, String body, BigDecimal price, String outTradeNo) {
		this(subject, body, price, outTradeNo, null, null);
	}


}
