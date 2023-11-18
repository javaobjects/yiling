package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 同步类型：1-流向订单
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportSyncTaskEnum {

	//流向订单
    FLOW_ORDER(1, "流向订单"),
    ;

    private Integer code;
    private String name;

    public static ReportSyncTaskEnum getByCode(Integer code) {
        for (ReportSyncTaskEnum e : ReportSyncTaskEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
