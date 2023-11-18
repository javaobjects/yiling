package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返利状态：1-待返利 2-已返利 3-部分返利
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportRebateStatusEnum {

	//待返利
	UN_REBATE(1, "待返利"),
	//已返利
    REBATED(2, "已返利"),
	//部分返利
	PROPORTION_REBATE(3, "部分返利"),
    ;

    private Integer code;
    private String name;

    public static ReportRebateStatusEnum getByCode(Integer code) {
        for (ReportRebateStatusEnum e : ReportRebateStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
