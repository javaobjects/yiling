package com.yiling.payment.enums;

import lombok.Getter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.enums
 * @date: 2021/10/22
 */
@Getter
public enum  PaySourceEnum {
    /**
     * 应用app
     */
    APP("app","app"),

    /**
     * 交通银行微信支付
     */
    BOCOM_PAY_WECHAT("bocomPayWechat","微信"),

    /**
     * 交通银行支付宝支付
     */
    BOCOM_PAY_ALIPAY("bocomPayAlipay","支付宝"),

    /**
     * 易宝-银行卡
     */
    YEE_PAY_BANK("yeePayBank", "银行卡"),
	/**
	 * 易宝-微信
	 */
	YEE_PAY_WECHAT("yeePayWechat", "微信"),
	/**
	 * 易宝-支付宝
	 */
	YEE_PAY_ALIPAY("yeePayAlipay", "支付宝");

    private final String source;
    private final String name;

    PaySourceEnum(String source, String name) {
        this.source = source;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.source;
    }

	public static PaySourceEnum getBySource(String source) {
		for (PaySourceEnum e : PaySourceEnum.values()) {

			if (e.getSource().equals(source)) {
				return e;
			}
		}
		return null;
	}
}
