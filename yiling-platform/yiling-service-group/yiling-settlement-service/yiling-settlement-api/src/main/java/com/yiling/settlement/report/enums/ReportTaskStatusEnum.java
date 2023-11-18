package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报表生成状态：1-生成中 2-生成成功 3-生成失败
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportTaskStatusEnum {

	//生成中
    IN_PRODUCTION(1, "生成中"),
	//生成成功
    TASK_SUCCESS(2, "生成成功"),
	//生成失败
    TASK_FAIL(3, "生成失败"),
    ;

    private Integer code;
    private String name;

    public static ReportTaskStatusEnum getByCode(Integer code) {
        for (ReportTaskStatusEnum e : ReportTaskStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
