package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报表类型：1-B2B返利 2-流向返利
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportTypeEnum {

	//b2b
	B2B(1, "b2b"),
	//流向
    FLOW(2, "流向"),
    ;

    private Integer code;
    private String name;

    public static ReportTypeEnum getByCode(Integer code) {
        for (ReportTypeEnum e : ReportTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
