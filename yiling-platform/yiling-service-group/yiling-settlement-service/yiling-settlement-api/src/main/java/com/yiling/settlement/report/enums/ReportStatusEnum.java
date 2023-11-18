package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报表状态：1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportStatusEnum {

	//待运营确认
	UN_OPERATOR_CONFIRM(1, "待运营确认"),
	//待财务确认
    UN_FINANCE_CONFIRM(2, "待财务确认"),
	//财务已确认
    FINANCE_CONFIRMED(3, "财务已确认"),
	//运营驳回
    OPERATOR_REJECT(4, "运营驳回"),
	//财务驳回
    FINANCE_REJECT(5, "财务驳回"),
	//管理员驳回
    ADMIN_REJECT(6, "管理员驳回"),
    ;

    private Integer code;
    private String name;

    public static ReportStatusEnum getByCode(Integer code) {
        for (ReportStatusEnum e : ReportStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
