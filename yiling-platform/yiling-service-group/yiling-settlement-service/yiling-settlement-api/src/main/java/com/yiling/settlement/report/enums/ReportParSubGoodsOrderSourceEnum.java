package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 活动&阶梯的订单来源：0-全部 1-B2B 2-自建平台 3-三方平台及其他渠道订单
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportParSubGoodsOrderSourceEnum {

	//全部
	ALL(0, "全部"),
	//B2B
	B2B(1, "B2B"),
	//自建平台
	OWN_SELF(2, "自建平台"),
	//三方平台及其他渠道订单
	OTHER(3, "三方平台及其他渠道订单"),
    ;

    private Integer code;
    private String name;

    public static ReportParSubGoodsOrderSourceEnum getByCode(Integer code) {
        for (ReportParSubGoodsOrderSourceEnum e : ReportParSubGoodsOrderSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
