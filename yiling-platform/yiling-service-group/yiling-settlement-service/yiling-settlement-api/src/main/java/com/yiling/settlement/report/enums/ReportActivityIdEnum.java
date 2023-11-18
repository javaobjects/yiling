package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报表子参数活动id枚举
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportActivityIdEnum {

	//特殊活动1
	FIRST(4L, "特殊活动1"),
	//特殊活动2
    SECOND(5L, "特殊活动2"),
	//特殊活动3
    THIRD(6L, "特殊活动3"),
	//特殊活动4
    FOURTH(7L, "特殊活动4"),
	//特殊活动5
    FIFTH(8L, "特殊活动5"),
	//小三元奖励
    XSY(9L, "小三元奖励"),
    ;

    private Long id;
    private String name;

    public static ReportActivityIdEnum getById(Long id) {
        for (ReportActivityIdEnum e : ReportActivityIdEnum.values()) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }
}
