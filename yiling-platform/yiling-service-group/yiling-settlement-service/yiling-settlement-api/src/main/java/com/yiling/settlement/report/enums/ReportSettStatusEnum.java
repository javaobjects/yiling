package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报表计算状态：1-未计算 2-已计算
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportSettStatusEnum {

	//未计算
	UN_CALCULATE(1, "未计算"),
	//已计算
    CALCULATED(2, "已计算"),
    ;

    private Integer code;
    private String name;

    public static ReportSettStatusEnum getByCode(Integer code) {
        for (ReportSettStatusEnum e : ReportSettStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
