package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 奖励类型：1-金额 2-百分比
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportRewardTypeEnum {

	//金额
	CASH(1, "金额"),
	//百分比
	PERCENTAGE(2, "百分比"),
    ;

    private Integer code;
    private String name;

    public static ReportRewardTypeEnum getByCode(Integer code) {
        for (ReportRewardTypeEnum e : ReportRewardTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
