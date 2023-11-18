package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日志类型：1-提交返利 2-运营确认 3-运营驳回 4-财务确认 5-财务驳回 6-调整金额 7-修改B2B订单标识 8-修改流向订单标识 9-管理员驳回 10-报表返利
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportLogTypeEnum {

    //提交返利
    CREATE(1, "提交返利"),
    //运营确认
    OPERATOR_CONFIRMED(2, "运营确认"),
    //运营驳回
    OPERATOR_REJECT(3, "运营驳回"),
    //财务确认
    FINANCE_CONFIRMED(4, "财务确认"),
    //财务驳回
    FINANCE_REJECT(5, "财务驳回"),
    //调整金额
    ADJUST(6, "调整金额"),
    //修改B2B订单标识
    UPDATE_B2B_ORDER_IDENT(7, "修改B2B订单标识"),
    //修改流向订单标识
    UPDATE_FLOW_ORDER_IDENT(8, "修改流向订单标识"),
    //管理员驳回
    ADMIN_REJECT(9, "管理员驳回"),
    //操作报表返利
    REPORT_REBATE(10, "操作报表返利"),
    ;

    private Integer code;
    private String name;

    public static ReportLogTypeEnum getByCode(Integer code) {
        for (ReportLogTypeEnum e : ReportLogTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
