package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报表订单标识状态：1-正常订单,2-无效订单,3-异常订单
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportOrderIdenEnum {

	//正常订单
	NORMAL(1, "正常订单"),
	//无效订单
    INVALID(2, "无效订单"),
	//异常订单
    ABNORMAL(3, "异常订单"),
    ;

    private Integer code;
    private String name;

    public static ReportOrderIdenEnum getByCode(Integer code) {
        for (ReportOrderIdenEnum e : ReportOrderIdenEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
