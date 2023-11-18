package com.yiling.payment.enums;

import java.util.Collections;
import java.util.List;

import com.beust.jcommander.internal.Lists;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.dto.enums
 * @date: 2021/10/15
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum {

    WEIXIN("wxPay", "微信支付") {
        @Override
        public List<PaySourceEnum> getPaySourceList() {

            return Collections.emptyList();
        }
    }, ALIPAY("aliPay", "支付宝支付") {
        @Override
        public List<PaySourceEnum> getPaySourceList() {

            return Collections.emptyList();
        }
    }, YEEPAY("yeePay", "易宝支付") {
        @Override
        public List<PaySourceEnum> getPaySourceList() {

            return ListUtil.toList(PaySourceEnum.YEE_PAY_ALIPAY, PaySourceEnum.YEE_PAY_BANK, PaySourceEnum.YEE_PAY_WECHAT);
        }
    }, BOCOMPAY("bocompay", "交通银行支付") {
        @Override
        public List<PaySourceEnum> getPaySourceList() {

            return ListUtil.toList(PaySourceEnum.BOCOM_PAY_ALIPAY, PaySourceEnum.BOCOM_PAY_WECHAT);
        }
    };

    private String code;
    private String name;

    public abstract List<PaySourceEnum> getPaySourceList();

    public static PayChannelEnum getByCode(String code) {
        for (PayChannelEnum e : PayChannelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
