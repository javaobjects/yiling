package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.enums
 * @date: 2021/10/22
 */
@Getter
@AllArgsConstructor
public enum TradeTypeEnum {

    DEPOSIT(1, "定金") {
        @Override
        public String getBody() {
            return "药品定金";
        }

        @Override
        public String getSubject() {
            return "付定金";
        }
    }, PAY(2, "订单支付") {
        @Override
        public String getBody() {
            return "药品采购";
        }

        @Override
        public String getSubject() {
            return "药品";
        }
    }, BACK(3, "还款") {
        @Override
        public String getBody() {
            return "账期还款";
        }

        @Override
        public String getSubject() {
            return "还款";
        }
    }, BALANCE(4, "支付尾款") {
        @Override
        public String getBody() {
            return "药品尾款";
        }

        @Override
        public String getSubject() {
            return "付尾款";
        }
    }, MEMBER(5, "购买会员") {
        @Override
        public String getBody() {
            return "购买会员";
        }

        @Override
        public String getSubject() {
            return "会员";
        }
    }, INQUIRY(6, "c端问诊") {
        @Override
        public String getBody() {
            return "问诊费用";
        }
        @Override
        public String getSubject() {
            return "问诊";
        }
    },PRESCRIPTION(7, "c端处方") {
        @Override
        public String getBody() {
            return "处方费用";
        }
        @Override
        public String getSubject() {
            return "处方";
        }
    };

    public abstract String getBody();

    public abstract String getSubject();

    private Integer code;
    private String name;

    public static TradeTypeEnum getByCode(Integer code) {
        for (TradeTypeEnum e : TradeTypeEnum.values()) {

            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
